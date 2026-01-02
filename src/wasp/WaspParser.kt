package wasp

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class WaspParser : PsiParser {

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        parseFile(builder)
        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseFile(builder: PsiBuilder) {
        while (!builder.eof()) {
            when (builder.tokenType) {
                Token.NEWLINE -> builder.advanceLexer()
                Token.COMMENT -> builder.advanceLexer()
                else -> parseStatement(builder)
            }
        }
    }

    private fun parseStatement(builder: PsiBuilder) {
        when (builder.tokenType) {
            Token.KEYWORD -> parseKeywordStatement(builder)
            Token.IDENTIFIER -> parseIdentifierStatement(builder)
            Token.TYPE -> parseTypeOrVariableDeclaration(builder)
            Token.STRING -> parseStringLiteral(builder)
            Token.NUMBER -> parseArithmeticExpression(builder)
            Token.NULL -> parseNullLiteral(builder)
            Token.LBRACE -> parseMapLiteral(builder)
            Token.LBRACKET -> parseArrayLiteral(builder)
            Token.LPAREN -> parseBlock(builder, Token.RPAREN)
            else -> {
                // Be permissive - consume the token without error
                builder.advanceLexer()
            }
        }
    }

    private fun parseKeywordStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        val keywordText = builder.tokenText
        builder.advanceLexer() // consume keyword

        // Check if this is a function declaration
        if (keywordText == "function") {
            parseFunctionDeclaration(builder, marker)
            return
        }

        // Parse the rest of the statement (condition/expression)
        while (!builder.eof() &&
            builder.tokenType != Token.NEWLINE &&
            builder.tokenType != Token.LBRACE &&
            builder.tokenType != Token.COLON
        ) {
            parseExpression(builder)
        }

        // Handle different block starters
        when (builder.tokenType) {
            Token.LBRACE -> parseBlock(builder, Token.RBRACE)
            Token.LPAREN -> parseBlock(builder, Token.RPAREN)
            Token.LBRACKET -> parseBlock(builder, Token.RBRACKET)
            Token.COLON -> {
                builder.advanceLexer() // consume ':'
                // Parse indented block (statements until dedent)
                parseIndentedBlock(builder)
            }
        }

        marker.done(WaspElementTypes.KEYWORD_STATEMENT)
    }

    private fun parseIdentifierStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume identifier

        // Check for function call
        if (builder.tokenType == Token.LPAREN) {
            parseArgumentList(builder)
            marker.done(WaspElementTypes.FUNCTION_CALL)
        } else if (builder.tokenType == Token.OPERATOR && builder.tokenText == "=") {
            // This is an assignment (possibly chained)
            parseAssignmentChain(builder)
            marker.done(WaspElementTypes.ASSIGNMENT)
        } else if (builder.tokenType == Token.NEWLINE || builder.eof()) {
            // Standalone identifier (function call without parens)
            marker.done(WaspElementTypes.IDENTIFIER_STATEMENT)
        } else {
            // Check for other expressions
            while (!builder.eof() && builder.tokenType != Token.NEWLINE) {
                parseExpression(builder)
            }
            marker.done(WaspElementTypes.IDENTIFIER_STATEMENT)
        }
    }

    private fun parseAssignmentChain(builder: PsiBuilder) {
        // Handle chained assignments: x=y=z=value
        while (builder.tokenType == Token.OPERATOR && builder.tokenText == "=") {
            builder.advanceLexer() // consume '='

            // Check if next is another identifier followed by '='
            if (builder.tokenType == Token.IDENTIFIER) {
                val nextPos = builder.mark()
                builder.advanceLexer()
                if (builder.tokenType == Token.OPERATOR && builder.tokenText == "=") {
                    nextPos.rollbackTo()
                    // Continue with nested assignment
                    val nestedMarker = builder.mark()
                    builder.advanceLexer() // consume identifier
                    parseAssignmentChain(builder)
                    nestedMarker.done(WaspElementTypes.ASSIGNMENT)
                } else {
                    nextPos.rollbackTo()
                    parseExpression(builder)
                }
            } else {
                parseExpression(builder)
            }
        }
    }


    private fun parseTypeOrVariableDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume type

        // Check if this is a standalone type or a variable declaration
        if (builder.tokenType == Token.IDENTIFIER) {
            // This is a variable declaration: "Int x = value"
            builder.advanceLexer() // consume variable name

            // Parse assignment
            if (builder.tokenType == Token.OPERATOR && builder.tokenText == "=") {
                builder.advanceLexer()
                parseExpression(builder)
            }

            marker.done(WaspElementTypes.VARIABLE_DECLARATION)
        } else {
            // This is a standalone type: "Int"
            marker.done(WaspElementTypes.TYPE_LITERAL)
        }
    }

    private fun parseFunctionDeclaration(builder: PsiBuilder, marker: PsiBuilder.Marker) {
        // Parse function name (optional - be permissive)
        if (builder.tokenType == Token.IDENTIFIER) {
            builder.advanceLexer()
        }

        // Parse parameter list (optional - be permissive)
        if (builder.tokenType == Token.LPAREN) {
            parseArgumentList(builder)
        }

        // Parse function body (optional - be permissive)
        if (builder.tokenType == Token.LBRACE) {
            parseBlock(builder, Token.RBRACE)
        }

        marker.done(WaspElementTypes.FUNCTION_DECLARATION)
    }

    private fun parseArrayLiteral(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '['

        while (!builder.eof() && builder.tokenType != Token.RBRACKET) {
            when (builder.tokenType) {
                Token.NEWLINE -> builder.advanceLexer()
                Token.COMMENT -> builder.advanceLexer()
                Token.COMMA -> builder.advanceLexer()
                else -> parseExpression(builder)
            }
        }

        // Consume closing bracket if present (be permissive if missing)
        if (builder.tokenType == Token.RBRACKET) {
            builder.advanceLexer()
        }

        marker.done(WaspElementTypes.ARRAY_LITERAL)
    }

    private fun parseMapLiteral(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '{'

        while (!builder.eof() && builder.tokenType != Token.RBRACE) {
            when (builder.tokenType) {
                Token.NEWLINE -> builder.advanceLexer()
                Token.COMMENT -> builder.advanceLexer()
                Token.COMMA -> builder.advanceLexer()
                else -> parseKeyValuePair(builder)
            }
        }

        // Consume closing brace if present (be permissive if missing)
        if (builder.tokenType == Token.RBRACE) {
            builder.advanceLexer()
        }

        marker.done(WaspElementTypes.MAP_LITERAL)
    }

    private fun parseKeyValuePair(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse key
        parseExpression(builder)

        // Parse colon (optional - be permissive)
        if (builder.tokenType == Token.COLON) {
            builder.advanceLexer()
            // Parse value
            parseExpression(builder)
        }

        marker.done(WaspElementTypes.KEY_VALUE_PAIR)
    }

    private fun parseArgumentList(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '('

        while (!builder.eof() && builder.tokenType != Token.RPAREN) {
            when (builder.tokenType) {
                Token.NEWLINE -> builder.advanceLexer()
                Token.COMMENT -> builder.advanceLexer()
                Token.COMMA -> builder.advanceLexer()
                else -> parseExpression(builder)
            }
        }

        // Consume closing paren if present (be permissive if missing)
        if (builder.tokenType == Token.RPAREN) {
            builder.advanceLexer()
        }

        marker.done(WaspElementTypes.ARGUMENT_LIST)
    }

    private fun parseExpression(builder: PsiBuilder) {
        when (builder.tokenType) {
            Token.IDENTIFIER -> {
                val marker = builder.mark()
                builder.advanceLexer()
                
                // Check if this is a function call
                if (builder.tokenType == Token.LPAREN) {
                    parseArgumentList(builder)
                    marker.done(WaspElementTypes.FUNCTION_CALL)
                } else {
                    marker.drop()
                }
            }
            Token.STRING -> parseStringLiteral(builder)
            Token.NUMBER -> parseNumberLiteral(builder)
            Token.NULL -> parseNullLiteral(builder)
            Token.OPERATOR -> builder.advanceLexer()
            Token.LPAREN -> parseBlock(builder, Token.RPAREN)
            Token.LBRACE -> parseMapLiteral(builder)
            Token.LBRACKET -> parseArrayLiteral(builder)
            Token.COLON -> builder.advanceLexer()
            Token.COMMA -> builder.advanceLexer()
            Token.DOT -> builder.advanceLexer()
            else -> builder.advanceLexer()
        }
    }

    private fun parseStringLiteral(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(WaspElementTypes.STRING_LITERAL)
    }

    private fun parseNumberLiteral(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(WaspElementTypes.NUMBER_LITERAL)
    }

    private fun parseNullLiteral(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(WaspElementTypes.NULL_LITERAL)
    }

    private fun parseArithmeticExpression(builder: PsiBuilder) {
        val marker = builder.mark()

        // Parse the first operand
        parseNumberLiteral(builder)

        // Parse operators and operands
        while (!builder.eof() &&
               builder.tokenType != Token.NEWLINE &&
               builder.tokenType == Token.OPERATOR) {
            builder.advanceLexer() // consume operator

            // Be permissive - try to parse what comes next
            when (builder.tokenType) {
                Token.NUMBER -> parseNumberLiteral(builder)
                Token.IDENTIFIER -> builder.advanceLexer()
                Token.LPAREN -> parseBlock(builder, Token.RPAREN)
                else -> break // Stop on unexpected token
            }
        }

        marker.done(WaspElementTypes.ARITHMETIC_EXPRESSION)
    }

    private fun symbol(elem: IElementType): String {
        if (elem == Token.LPAREN) return "("
        if (elem == Token.RPAREN) return ")"
        if (elem == Token.LBRACE) return "{"
        if (elem == Token.RBRACE) return "}"
        if (elem == Token.LBRACKET) return "["
        if (elem == Token.RBRACKET) return "]"
        return elem.toString()
    }

    private fun parseBlock(builder: PsiBuilder, rparen: IElementType) {
        val marker = builder.mark()
        builder.advanceLexer() // consume opening token

        while (!builder.eof() && builder.tokenType != rparen) {
            when (builder.tokenType) {
                Token.NEWLINE -> builder.advanceLexer()
                Token.COMMENT -> builder.advanceLexer()
                else -> parseStatement(builder)
            }
        }

        // Consume closing token if present (be permissive if missing)
        if (builder.tokenType == rparen) {
            builder.advanceLexer()
        }

        marker.done(WaspElementTypes.BLOCK)
    }

    private fun parseIndentedBlock(builder: PsiBuilder) {
        val marker = builder.mark()

        // Skip newline after colon
        if (builder.tokenType == Token.NEWLINE) {
            builder.advanceLexer()
        }

        // Parse statements until we hit a non-indented line or EOF
        // Since we don't track indentation in the lexer, we'll parse
        // statements until we hit a dedent indicator or EOF
        // For now, just parse statements on subsequent lines
        var hasStatements = false
        while (!builder.eof()) {
            when (builder.tokenType) {
                Token.NEWLINE -> {
                    builder.advanceLexer()
                    // Check if next line is dedented (starts with non-whitespace keyword/identifier at column 0)
                    // For simplicity, we'll just continue parsing until we hit EOF or a clear dedent
                }
                Token.COMMENT -> builder.advanceLexer()
                else -> {
                    parseStatement(builder)
                    hasStatements = true
                    // After a statement, check for newline
                    if (builder.tokenType == Token.NEWLINE) {
                        continue
                    } else {
                        // No more statements in the indented block
                        break
                    }
                }
            }
        }

        marker.done(WaspElementTypes.BLOCK)
    }

}
