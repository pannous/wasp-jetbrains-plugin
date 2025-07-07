package com.pannous.wasp

import com.intellij.psi.tree.IElementType

object WaspElementTypes {
    val KEYWORD_STATEMENT = IElementType("WASP_KEYWORD_STATEMENT", WaspLanguage.INSTANCE)
    val IDENTIFIER_STATEMENT = IElementType("WASP_IDENTIFIER_STATEMENT", WaspLanguage.INSTANCE)
    val STRING_LITERAL = IElementType("WASP_STRING_LITERAL", WaspLanguage.INSTANCE)
    val NUMBER_LITERAL = IElementType("WASP_NUMBER_LITERAL", WaspLanguage.INSTANCE)
    val BLOCK = IElementType("WASP_BLOCK", WaspLanguage.INSTANCE)
    val PAREN_EXPRESSION = IElementType("WASP_PAREN_EXPRESSION", WaspLanguage.INSTANCE)
    val LIST_EXPRESSION = IElementType("WASP_LIST_EXPRESSION", WaspLanguage.INSTANCE)
}