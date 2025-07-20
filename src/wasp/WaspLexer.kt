package wasp

import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class WaspLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var start = 0
    private var end = 0
    private var position = 0
    private var state = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null

    companion object {
        // @formatter:off
        // or use custom line breaks aka manual enters like here
        // @formatter:on

        private val TYPES = setOf(
            // Primitive types
            "int", "real", "float", "double", "bool", "char", "string", "byte","codepoint",
            // Collection types
            "array", "list", "set", "map", "dict", "tuple", "vector", "matrix","node",
            // Reference types
            "object", "class", "interface", "struct", "union", "enum",
            // Function types
            "function", "func", "lambda", "closure", "method", "procedure",
            // Memory types
            "pointer", "reference", "ref",  "externref", "ptr", "address",
            // Numeric types
            "number", "integer", "decimal", "fraction", "complex", "rational",
            "int8", "int16", "int32", "int64", "uint8", "uint16", "uint32", "uint64",
            "float32", "float64", "long", "short", "unsigned", "signed",
            // Special types
            "void", "null", "nil", "nothing", "unit", "any", "auto", "var",
            "const", "static", "final", "readonly", "mutable", "immutable",
            // Domain-specific types
            "time", "date", "datetime", "duration", "url", "path", "file",
            "json", "xml", "html", "css", "regex", "uuid", "hash", "binary"
        )

        private val KEYWORDS = setOf(
            "def","fun","fn", // function declaration keywords (fn can also be a type)
            "extern","export",
            "if", "else", "elif", "for", "while", "in", "not",
            "and", "or", "def", "class", "return", "import", "include","use", "from", "as", "pass",
            "break", "continue", "try", "except", "finally", "raise", "with", "yield",
            "lambda", "global", "True", "False", "None", "nil", "null", "is", "assert",
            "del", "fun", "in", "on", "of", "to", "with", "while", "it", "that", "which",
            "type", "id", "π", "τ"
        )

        private val OPERATORS = setOf(
            "+", "-", "*", "/", "//", "%", "**", "=", "==", "!=", "<", ">", "<=", ">=",
            "<<", ">>", "&", "|", "^", "~", "+=", "-=", "*=", "/=", "//=", "%=", "**=",
            "&=", "|=", "^=", "<<=", ">>=", "≈", "√", "ʃ", "×", "⁰", "⁻¹", "²", "³", "∈", "∊"
        )

        private val NULL_LITERALS = setOf("null", "nil", "None")
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.start = startOffset
        this.end = endOffset
        this.position = startOffset
        this.state = initialState
        advance()
    }

    override fun advance() {
        if (position >= end) {
            tokenType = null
            return
        }

        tokenStart = position
        tokenEnd = position

        when (val char = buffer[position]) {
            ' ', '\t' -> {
                skipWhitespace()
                tokenType = TokenType.WHITE_SPACE
            }

            '\n', '\r' -> {
                skipNewline()
                tokenType = Token.NEWLINE
            }

            '#' -> {
                skipHashComment()
                tokenType = Token.COMMENT
            }

            '/' -> {
                if (position + 1 < end) {
                    when (buffer[position + 1]) {
                        '/' -> {
                            skipSingleLineComment()
                            tokenType = Token.COMMENT
                        }

                        '*' -> {
                            skipMultiLineComment()
                            tokenType = Token.COMMENT
                        }

                        else -> {
                            if (skipOperator()) {
                                tokenType = Token.OPERATOR
                            } else {
                                position++
                                tokenEnd = position
                                tokenType = TokenType.BAD_CHARACTER
                            }
                        }
                    }
                } else {
                    if (skipOperator()) {
                        tokenType = Token.OPERATOR
                    } else {
                        position++
                        tokenEnd = position
                        tokenType = TokenType.BAD_CHARACTER
                    }
                }
            }

            '"', '\'', '`', '“', '‘' -> { // todo closing , '«'
                skipString(char)
                tokenType = Token.STRING
            }

            '{' -> {
                position++
                tokenEnd = position
                tokenType = Token.LBRACE
            }

            '}' -> {
                position++
                tokenEnd = position
                tokenType = Token.RBRACE
            }

            '(' -> {
                position++
                tokenEnd = position
                tokenType = Token.LPAREN
            }

            ')' -> {
                position++
                tokenEnd = position
                tokenType = Token.RPAREN
            }

            '[' -> {
                position++
                tokenEnd = position
                tokenType = Token.LBRACKET
            }

            ']' -> {
                position++
                tokenEnd = position
                tokenType = Token.RBRACKET
            }

            ',' -> {
                position++
                tokenEnd = position
                tokenType = Token.COMMA
            }

            ':' -> {
                position++
                tokenEnd = position
                tokenType = Token.COLON
            }

            ';' -> {
                position++
                tokenEnd = position
                tokenType = Token.SEMICOLON
            }

            '.' -> {
                position++
                tokenEnd = position
                tokenType = Token.DOT
            }

            in '0'..'9', 'π', 'τ' -> {
                skipNumber()
                tokenType = Token.NUMBER
            }

            in 'a'..'z', in 'A'..'Z', '_' -> {
                skipIdentifier()
                val text = buffer.subSequence(tokenStart, tokenEnd).toString()
                tokenType = when {
                    NULL_LITERALS.contains(text) -> Token.NULL
                    KEYWORDS.contains(text) -> Token.KEYWORD
                    TYPES.contains(text) -> Token.TYPE
                    else -> Token.IDENTIFIER
                }
            }

            else -> {
                if (skipOperator()) {
                    tokenType = Token.OPERATOR
                } else {
                    position++
                    tokenEnd = position
                    tokenType = TokenType.BAD_CHARACTER
                }
            }
        }
    }

    private fun skipWhitespace() {
        while (position < end && (buffer[position] == ' ' || buffer[position] == '\t')) {
            position++
        }
        tokenEnd = position
    }

    private fun skipNewline() {
        if (position < end && buffer[position] == '\r') {
            position++
            if (position < end && buffer[position] == '\n') {
                position++
            }
        } else if (position < end && buffer[position] == '\n') {
            position++
        }
        tokenEnd = position
    }

    private fun skipHashComment() {
        while (position < end && buffer[position] != '\n' && buffer[position] != '\r') {
            position++
        }
        tokenEnd = position
    }

    private fun skipSingleLineComment() {
        position += 2 // skip '//'
        while (position < end && buffer[position] != '\n' && buffer[position] != '\r') {
            position++
        }
        tokenEnd = position
    }

    private fun skipMultiLineComment() {
        position += 2 // skip '/*'
        while (position < end - 1) {
            if (buffer[position] == '*' && buffer[position + 1] == '/') {
                position += 2 // skip '*/'
                break
            }
            position++
        }
        tokenEnd = position
    }

    private fun skipString(quote: Char) {
        position++ // skip opening quote
        while (position < end && buffer[position] != quote) {
            if (buffer[position] == '\\' && position + 1 < end) {
                position += 2 // skip escaped character
            } else {
                position++
            }
        }
        if (position < end) {
            position++ // skip closing quote
        }
        tokenEnd = position
    }

    private fun skipNumber() {
        while (position < end && (buffer[position].isDigit() || buffer[position] == '.' || buffer[position] == '_')) {
            position++
        }
        if (position < end && (buffer[position] == 'f' || buffer[position] == 'l' || buffer[position] == 'c'))  // c-compatible suffixes
            position++
        if (position < end && (buffer[position] == 'π' || buffer[position] == 'τ'))
            position++
        tokenEnd = position
    }

    private fun skipIdentifier() {
        while (position < end && (buffer[position].isLetterOrDigit() || buffer[position] == '_')) {
            position++
        }
        tokenEnd = position
    }

    private fun skipOperator(): Boolean {
        val remainingBuffer = buffer.subSequence(position, end).toString()
        val longestMatch = OPERATORS.filter { remainingBuffer.startsWith(it) }
            .maxByOrNull { it.length }

        return if (longestMatch != null) {
            position += longestMatch.length
            tokenEnd = position
            true
        } else {
            false
        }
    }

    override fun getState(): Int = state

    override fun getTokenType(): IElementType? = tokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = end
}
