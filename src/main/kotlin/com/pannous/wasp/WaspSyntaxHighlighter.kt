package com.pannous.wasp

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class WaspSyntaxHighlighter : SyntaxHighlighterBase() {
    
    companion object {
        val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "WASP_KEYWORD", 
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val STRING = TextAttributesKey.createTextAttributesKey(
            "WASP_STRING", 
            DefaultLanguageHighlighterColors.STRING
        )
        
        val NUMBER = TextAttributesKey.createTextAttributesKey(
            "WASP_NUMBER", 
            DefaultLanguageHighlighterColors.NUMBER
        )
        
        val COMMENT = TextAttributesKey.createTextAttributesKey(
            "WASP_COMMENT", 
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        
        val IDENTIFIER = TextAttributesKey.createTextAttributesKey(
            "WASP_IDENTIFIER", 
            DefaultLanguageHighlighterColors.IDENTIFIER
        )
        
        val OPERATOR = TextAttributesKey.createTextAttributesKey(
            "WASP_OPERATOR", 
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val BRACES = TextAttributesKey.createTextAttributesKey(
            "WASP_BRACES", 
            DefaultLanguageHighlighterColors.BRACES
        )
        
        val BRACKETS = TextAttributesKey.createTextAttributesKey(
            "WASP_BRACKETS", 
            DefaultLanguageHighlighterColors.BRACKETS
        )
        
        val PARENTHESES = TextAttributesKey.createTextAttributesKey(
            "WASP_PARENTHESES", 
            DefaultLanguageHighlighterColors.PARENTHESES
        )
        
        val COMMA = TextAttributesKey.createTextAttributesKey(
            "WASP_COMMA", 
            DefaultLanguageHighlighterColors.COMMA
        )
        
        val DOT = TextAttributesKey.createTextAttributesKey(
            "WASP_DOT", 
            DefaultLanguageHighlighterColors.DOT
        )
        
        val SEMICOLON = TextAttributesKey.createTextAttributesKey(
            "WASP_SEMICOLON", 
            DefaultLanguageHighlighterColors.SEMICOLON
        )
        
        val COLON = TextAttributesKey.createTextAttributesKey(
            "WASP_COLON", 
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
    }
    
    override fun getHighlightingLexer(): Lexer = WaspLexer()
    
    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when (tokenType) {
            WaspTokenTypes.KEYWORD -> arrayOf(KEYWORD)
            WaspTokenTypes.STRING -> arrayOf(STRING)
            WaspTokenTypes.NUMBER -> arrayOf(NUMBER)
            WaspTokenTypes.COMMENT -> arrayOf(COMMENT)
            WaspTokenTypes.IDENTIFIER -> arrayOf(IDENTIFIER)
            WaspTokenTypes.OPERATOR -> arrayOf(OPERATOR)
            WaspTokenTypes.LBRACE, WaspTokenTypes.RBRACE -> arrayOf(BRACES)
            WaspTokenTypes.LBRACKET, WaspTokenTypes.RBRACKET -> arrayOf(BRACKETS)
            WaspTokenTypes.LPAREN, WaspTokenTypes.RPAREN -> arrayOf(PARENTHESES)
            WaspTokenTypes.COMMA -> arrayOf(COMMA)
            WaspTokenTypes.DOT -> arrayOf(DOT)
            WaspTokenTypes.SEMICOLON -> arrayOf(SEMICOLON)
            WaspTokenTypes.COLON -> arrayOf(COLON)
            TokenType.BAD_CHARACTER -> arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "WASP_BAD_CHARACTER", 
                    DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE
                )
            )
            else -> emptyArray()
        }
    }
}