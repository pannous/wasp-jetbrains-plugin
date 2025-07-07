package com.pannous.wasp

import com.intellij.psi.tree.IElementType

object Token {
    val KEYWORD = IElementType("WASP_KEYWORD", WaspLanguage.INSTANCE)
    val IDENTIFIER = IElementType("WASP_IDENTIFIER", WaspLanguage.INSTANCE)
    val STRING = IElementType("WASP_STRING", WaspLanguage.INSTANCE)
    val NUMBER = IElementType("WASP_NUMBER", WaspLanguage.INSTANCE)
    val COMMENT = IElementType("WASP_COMMENT", WaspLanguage.INSTANCE)
    val OPERATOR = IElementType("WASP_OPERATOR", WaspLanguage.INSTANCE)
    val NEWLINE = IElementType("WASP_NEWLINE", WaspLanguage.INSTANCE)
    
    // Delimiters
    val LBRACE = IElementType("WASP_LBRACE", WaspLanguage.INSTANCE)
    val RBRACE = IElementType("WASP_RBRACE", WaspLanguage.INSTANCE)
    val LPAREN = IElementType("WASP_LPAREN", WaspLanguage.INSTANCE)
    val RPAREN = IElementType("WASP_RPAREN", WaspLanguage.INSTANCE)
    val LBRACKET = IElementType("WASP_LBRACKET", WaspLanguage.INSTANCE)
    val RBRACKET = IElementType("WASP_RBRACKET", WaspLanguage.INSTANCE)
    val COMMA = IElementType("WASP_COMMA", WaspLanguage.INSTANCE)
    val COLON = IElementType("WASP_COLON", WaspLanguage.INSTANCE)
    val SEMICOLON = IElementType("WASP_SEMICOLON", WaspLanguage.INSTANCE)
    val DOT = IElementType("WASP_DOT", WaspLanguage.INSTANCE)
}
