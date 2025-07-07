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
                WaspTokenTypes.NEWLINE -> builder.advanceLexer()
                WaspTokenTypes.COMMENT -> builder.advanceLexer()
                else -> parseStatement(builder)
            }
        }
    }
    
    private fun parseStatement(builder: PsiBuilder) {
        when (builder.tokenType) {
            WaspTokenTypes.KEYWORD -> parseKeywordStatement(builder)
            WaspTokenTypes.IDENTIFIER -> parseIdentifierStatement(builder)
            WaspTokenTypes.STRING -> parseStringLiteral(builder)
            WaspTokenTypes.NUMBER -> parseNumberLiteral(builder)
            WaspTokenTypes.LBRACE -> parseBlock(builder)
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
        while (!builder.eof() && 
               builder.tokenType != WaspTokenTypes.NEWLINE && 
               builder.tokenType != WaspTokenTypes.LBRACE) {
            parseExpression(builder)
        }
        
        if (builder.tokenType == WaspTokenTypes.LBRACE) {
            parseBlock(builder)
        }
        
        marker.done(WaspElementTypes.KEYWORD_STATEMENT)
    }
    
    private fun parseIdentifierStatement(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume identifier
        
        // Check for assignment or function call
        while (!builder.eof() && 
               builder.tokenType != WaspTokenTypes.NEWLINE) {
            parseExpression(builder)
        }
        
        marker.done(WaspElementTypes.IDENTIFIER_STATEMENT)
    }
    
    private fun parseExpression(builder: PsiBuilder) {
        when (builder.tokenType) {
            WaspTokenTypes.IDENTIFIER -> builder.advanceLexer()
            WaspTokenTypes.STRING -> parseStringLiteral(builder)
            WaspTokenTypes.NUMBER -> parseNumberLiteral(builder)
            WaspTokenTypes.OPERATOR -> builder.advanceLexer()
            WaspTokenTypes.LPAREN -> parseParenExpression(builder)
            WaspTokenTypes.LBRACKET -> parseListExpression(builder)
            WaspTokenTypes.LBRACE -> parseBlock(builder)
            WaspTokenTypes.COLON -> builder.advanceLexer()
            WaspTokenTypes.COMMA -> builder.advanceLexer()
            WaspTokenTypes.DOT -> builder.advanceLexer()
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
    
    private fun parseBlock(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '{'
        
        while (!builder.eof() && builder.tokenType != WaspTokenTypes.RBRACE) {
            when (builder.tokenType) {
                WaspTokenTypes.NEWLINE -> builder.advanceLexer()
                WaspTokenTypes.COMMENT -> builder.advanceLexer()
                else -> parseStatement(builder)
            }
        }
        
        if (builder.tokenType == WaspTokenTypes.RBRACE) {
            builder.advanceLexer()
        } else {
            builder.error("Expected '}'")
        }
        
        marker.done(WaspElementTypes.BLOCK)
    }
    
    private fun parseParenExpression(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '('
        
        while (!builder.eof() && builder.tokenType != WaspTokenTypes.RPAREN) {
            parseExpression(builder)
        }
        
        if (builder.tokenType == WaspTokenTypes.RPAREN) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ')'")
        }
        
        marker.done(WaspElementTypes.PAREN_EXPRESSION)
    }
    
    private fun parseListExpression(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '['
        
        while (!builder.eof() && builder.tokenType != WaspTokenTypes.RBRACKET) {
            parseExpression(builder)
        }
        
        if (builder.tokenType == WaspTokenTypes.RBRACKET) {
            builder.advanceLexer()
        } else {
            builder.error("Expected ']'")
        }
        
        marker.done(WaspElementTypes.LIST_EXPRESSION)
    }
}