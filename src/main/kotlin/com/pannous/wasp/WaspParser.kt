package com.pannous.wasp

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
            Token.STRING -> parseStringLiteral(builder)
            Token.NUMBER -> parseNumberLiteral(builder)
            Token.LBRACE -> parseBlock(builder, Token.RBRACE)
            Token.LBRACKET -> parseBlock(builder, Token.RBRACKET)
            Token.LPAREN -> parseBlock(builder, Token.RPAREN)
            else -> {
                builder.error("Unexpected token")
                builder.advanceLexer()
            }
        }
    }

    private fun parseKeywordStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume keyword

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
        builder.advanceLexer() // consume identifier

        // Check for assignment or function call
        while (!builder.eof() && builder.tokenType != Token.NEWLINE) {
            parseExpression(builder)
        }

        marker.done(WaspElementTypes.IDENTIFIER_STATEMENT)
    }

    private fun parseExpression(builder: PsiBuilder) {
        when (builder.tokenType) {
            Token.IDENTIFIER -> builder.advanceLexer()
            Token.STRING -> parseStringLiteral(builder)
            Token.NUMBER -> parseNumberLiteral(builder)
            Token.OPERATOR -> builder.advanceLexer()
            Token.LPAREN -> parseBlock(builder, Token.RPAREN)
            Token.LBRACE -> parseBlock(builder, Token.RBRACE)
            Token.LBRACKET -> parseBlock(builder, Token.RBRACKET)
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
