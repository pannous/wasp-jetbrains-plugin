package wasp

import com.intellij.psi.tree.IElementType

object WaspElementTypes {
    val KEYWORD_STATEMENT = IElementType("WASP_KEYWORD_STATEMENT", WaspLanguage.INSTANCE)
    val IDENTIFIER_STATEMENT = IElementType("WASP_IDENTIFIER_STATEMENT", WaspLanguage.INSTANCE)
    val TYPE_STATEMENT = IElementType("WASP_TYPE_STATEMENT", WaspLanguage.INSTANCE)
    val VARIABLE_DECLARATION = IElementType("WASP_VARIABLE_DECLARATION", WaspLanguage.INSTANCE)
    val FUNCTION_CALL = IElementType("WASP_FUNCTION_CALL", WaspLanguage.INSTANCE)
    val ARGUMENT_LIST = IElementType("WASP_ARGUMENT_LIST", WaspLanguage.INSTANCE)
    val STRING_LITERAL = IElementType("WASP_STRING_LITERAL", WaspLanguage.INSTANCE)
    val NUMBER_LITERAL = IElementType("WASP_NUMBER_LITERAL", WaspLanguage.INSTANCE)
    val ARRAY_LITERAL = IElementType("WASP_ARRAY_LITERAL", WaspLanguage.INSTANCE)
    val MAP_LITERAL = IElementType("WASP_MAP_LITERAL", WaspLanguage.INSTANCE)
    val KEY_VALUE_PAIR = IElementType("WASP_KEY_VALUE_PAIR", WaspLanguage.INSTANCE)
    val ARITHMETIC_EXPRESSION = IElementType("WASP_ARITHMETIC_EXPRESSION", WaspLanguage.INSTANCE)
    val BLOCK = IElementType("WASP_BLOCK", WaspLanguage.INSTANCE)
    val PAREN_EXPRESSION = IElementType("WASP_PAREN_EXPRESSION", WaspLanguage.INSTANCE)
    val LIST_EXPRESSION = IElementType("WASP_LIST_EXPRESSION", WaspLanguage.INSTANCE)
    val ASSIGNMENT = IElementType("WASP_ASSIGNMENT", WaspLanguage.INSTANCE)
    val NULL_LITERAL = IElementType("WASP_NULL_LITERAL", WaspLanguage.INSTANCE)
}