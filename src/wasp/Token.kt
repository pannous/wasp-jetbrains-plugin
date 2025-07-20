package wasp

import com.intellij.psi.tree.IElementType

object Token {
    val KEYWORD = IElementType("KEYWORD", WaspLanguage.INSTANCE)
    val IDENTIFIER = IElementType("IDENTIFIER", WaspLanguage.INSTANCE)
    val TYPE = IElementType("TYPE", WaspLanguage.INSTANCE)
    val STRING = IElementType("STRING", WaspLanguage.INSTANCE)
    val NUMBER = IElementType("NUMBER", WaspLanguage.INSTANCE)
    val COMMENT = IElementType("COMMENT", WaspLanguage.INSTANCE)
    val OPERATOR = IElementType("OPERATOR", WaspLanguage.INSTANCE)
    val NEWLINE = IElementType("NEWLINE", WaspLanguage.INSTANCE)
    
    // Delimiters
    val LBRACE = IElementType("LBRACE", WaspLanguage.INSTANCE)
    val RBRACE = IElementType("RBRACE", WaspLanguage.INSTANCE)
    val LPAREN = IElementType("LPAREN", WaspLanguage.INSTANCE)
    val RPAREN = IElementType("RPAREN", WaspLanguage.INSTANCE)
    val LBRACKET = IElementType("LBRACKET", WaspLanguage.INSTANCE)
    val RBRACKET = IElementType("RBRACKET", WaspLanguage.INSTANCE)
    val COMMA = IElementType("COMMA", WaspLanguage.INSTANCE)
    val COLON = IElementType("COLON", WaspLanguage.INSTANCE)
    val SEMICOLON = IElementType("SEMICOLON", WaspLanguage.INSTANCE)
    val DOT = IElementType("DOT", WaspLanguage.INSTANCE)
    val NULL = IElementType("NULL", WaspLanguage.INSTANCE)
}
