package wasp

import com.intellij.psi.tree.IElementType

object WaspElementTypes {
    val KEYWORD_STATEMENT = IElementType("KEYWORD_STATEMENT", WaspLanguage.INSTANCE)
    val IDENTIFIER_STATEMENT = IElementType("IDENTIFIER_STATEMENT", WaspLanguage.INSTANCE)
    val VARIABLE_DECLARATION = IElementType("VARIABLE_DECLARATION", WaspLanguage.INSTANCE)
    val FUNCTION_CALL = IElementType("FUNCTION_CALL", WaspLanguage.INSTANCE)
    val ARGUMENT_LIST = IElementType("ARGUMENT_LIST", WaspLanguage.INSTANCE)
    val STRING_LITERAL = IElementType("STRING_LITERAL", WaspLanguage.INSTANCE)
    val NUMBER_LITERAL = IElementType("NUMBER_LITERAL", WaspLanguage.INSTANCE)
    val ARRAY_LITERAL = IElementType("ARRAY_LITERAL", WaspLanguage.INSTANCE)
    val MAP_LITERAL = IElementType("MAP_LITERAL", WaspLanguage.INSTANCE)
    val KEY_VALUE_PAIR = IElementType("KEY_VALUE_PAIR", WaspLanguage.INSTANCE)
    val ARITHMETIC_EXPRESSION = IElementType("ARITHMETIC_EXPRESSION", WaspLanguage.INSTANCE)
    val BLOCK = IElementType("BLOCK", WaspLanguage.INSTANCE)
    val PAREN_EXPRESSION = IElementType("PAREN_EXPRESSION", WaspLanguage.INSTANCE)
    val LIST_EXPRESSION = IElementType("LIST_EXPRESSION", WaspLanguage.INSTANCE)
    val ASSIGNMENT = IElementType("ASSIGNMENT", WaspLanguage.INSTANCE)
    val NULL_LITERAL = IElementType("NULL_LITERAL", WaspLanguage.INSTANCE)
    val FUNCTION_DECLARATION = IElementType("FUNCTION_DECLARATION", WaspLanguage.INSTANCE)
}
