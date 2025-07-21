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
            Token.TYPE -> parseVariableDeclaration(builder)
            Token.STRING -> parseStringLiteral(builder)
            Token.NUMBER -> parseArithmeticExpression(builder)
            Token.LBRACE -> parseMapLiteral(builder)
            Token.LBRACKET -> parseArrayLiteral(builder)
            Token.LPAREN -> parseBlock(builder, Token.RPAREN)
            else -> {
                builder.error("Unexpected token")
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

        // Parse the rest of the statement based on context
        val type = builder.tokenType
        while (!builder.eof() &&
            type != Token.NEWLINE &&
            type != Token.LBRACE
        ) {
            parseExpression(builder)
        }

        if (type == Token.LBRACE) parseBlock(builder, Token.RBRACE)
        if (type == Token.LPAREN) parseBlock(builder, Token.RPAREN)
        if (type == Token.LBRACKET) parseBlock(builder, Token.RBRACKET)

        marker.done(WaspElementTypes.KEYWORD_STATEMENT)
    }

    private fun parseIdentifierStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        val identifierText = builder.tokenText
        
        // Check if identifier starts with capital letter - treat as TYPE
        if (identifierText != null && identifierText.isNotEmpty() && identifierText[0].isUpperCase()) {
            builder.advanceLexer() // consume identifier
            
            // Parse the rest of the type statement
            while (!builder.eof() && builder.tokenType != Token.NEWLINE) {
                parseExpression(builder)
            }
            
            marker.done(WaspElementTypes.TYPE_STATEMENT)
            return
        }
        
        builder.advanceLexer() // consume identifier

        // Check for function call
        if (builder.tokenType == Token.LPAREN) {
            parseArgumentList(builder)
            marker.done(WaspElementTypes.FUNCTION_CALL)
        } else if (builder.tokenType == Token.OPERATOR && builder.tokenText == "=") {
            // This is an assignment
            builder.advanceLexer() // consume '='
            parseExpression(builder)
            marker.done(WaspElementTypes.ASSIGNMENT)
        } else {
            // Check for other expressions
            while (!builder.eof() && builder.tokenType != Token.NEWLINE) {
                parseExpression(builder)
            }
            marker.done(WaspElementTypes.IDENTIFIER_STATEMENT)
        }
    }

    private fun parseTypeStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume type

        // Parse the rest of the type declaration
        while (!builder.eof() && builder.tokenType != Token.NEWLINE) {
            parseExpression(builder)
        }

        marker.done(WaspElementTypes.TYPE_STATEMENT)
    }

    private fun parseVariableDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume type

        // Parse variable name
        if (builder.tokenType == Token.IDENTIFIER) {
            builder.advanceLexer()
        } else {
            builder.error("Expected variable name")
        }

        // Parse assignment
        if (builder.tokenType == Token.OPERATOR && builder.tokenText == "=") {
            builder.advanceLexer()
            parseExpression(builder)
        }

        marker.done(WaspElementTypes.VARIABLE_DECLARATION)
    }

    private fun parseFunctionDeclaration(builder: PsiBuilder, marker: PsiBuilder.Marker) {
        // Parse function name
        if (builder.tokenType == Token.IDENTIFIER) {
            builder.advanceLexer()
        } else {
            builder.error("Expected function name")
        }

        // Parse parameter list
        if (builder.tokenType == Token.LPAREN) {
            parseArgumentList(builder)
        } else {
            builder.error("Expected '(' after function name")
        }

        // Parse function body
        if (builder.tokenType == Token.LBRACE) {
            parseBlock(builder, Token.RBRACE)
        } else {
            builder.error("Expected '{' for function body")
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

        if (builder.tokenType == Token.RBRACKET) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ']'")
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

        if (builder.tokenType == Token.RBRACE) {
            builder.advanceLexer()
        } else {
            builder.error("Expected '}'")
        }

        marker.done(WaspElementTypes.MAP_LITERAL)
    }

    private fun parseKeyValuePair(builder: PsiBuilder) {
        val marker = builder.mark()
        
        // Parse key
        parseExpression(builder)
        
        // Parse colon
        if (builder.tokenType == Token.COLON) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ':'")
        }
        
        // Parse value
        parseExpression(builder)
        
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

        if (builder.tokenType == Token.RPAREN) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ')'")
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
            
            when (builder.tokenType) {
                Token.NUMBER -> parseNumberLiteral(builder)
                Token.IDENTIFIER -> builder.advanceLexer()
                Token.LPAREN -> parseBlock(builder, Token.RPAREN)
                else -> {
                    builder.error("Expected number, identifier, or parenthesized expression")
                    break
                }
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
        builder.advanceLexer() // consume '{'

        while (!builder.eof() && builder.tokenType != rparen) {
            when (builder.tokenType) {
                Token.NEWLINE -> builder.advanceLexer()
                Token.COMMENT -> builder.advanceLexer()
                else -> parseStatement(builder)
            }
        }

        if (builder.tokenType == rparen) {
            builder.advanceLexer()
        } else {
            builder.error("Expected " + symbol(rparen))
        }

        marker.done(WaspElementTypes.BLOCK)
    }

}
