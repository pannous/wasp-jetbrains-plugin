package wasp

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class WaspSyntaxHighlighter : SyntaxHighlighterBase() {
    
    companion object {
        val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "KEYWORD", 
            DefaultLanguageHighlighterColors.KEYWORD
        )
        
        val STRING = TextAttributesKey.createTextAttributesKey(
            "STRING", 
            DefaultLanguageHighlighterColors.STRING
        )
        
        val NUMBER = TextAttributesKey.createTextAttributesKey(
            "NUMBER", 
            DefaultLanguageHighlighterColors.NUMBER
        )
        
        val COMMENT = TextAttributesKey.createTextAttributesKey(
            "COMMENT", 
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        
        val IDENTIFIER = TextAttributesKey.createTextAttributesKey(
            "IDENTIFIER", 
            DefaultLanguageHighlighterColors.IDENTIFIER
        )
        
        val TYPE = TextAttributesKey.createTextAttributesKey(
            "TYPE", 
            DefaultLanguageHighlighterColors.CLASS_NAME
        )
        
        val FUNCTION_CALL = TextAttributesKey.createTextAttributesKey(
            "FUNCTION_CALL", 
            DefaultLanguageHighlighterColors.FUNCTION_CALL
        )
        
        val OPERATOR = TextAttributesKey.createTextAttributesKey(
            "OPERATOR", 
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        
        val BRACES = TextAttributesKey.createTextAttributesKey(
            "BRACES", 
            DefaultLanguageHighlighterColors.BRACES
        )
        
        val BRACKETS = TextAttributesKey.createTextAttributesKey(
            "BRACKETS", 
            DefaultLanguageHighlighterColors.BRACKETS
        )
        
        val PARENTHESES = TextAttributesKey.createTextAttributesKey(
            "PARENTHESES", 
            DefaultLanguageHighlighterColors.PARENTHESES
        )
        
        val COMMA = TextAttributesKey.createTextAttributesKey(
            "COMMA", 
            DefaultLanguageHighlighterColors.COMMA
        )
        
        val DOT = TextAttributesKey.createTextAttributesKey(
            "DOT", 
            DefaultLanguageHighlighterColors.DOT
        )
        
        val SEMICOLON = TextAttributesKey.createTextAttributesKey(
            "SEMICOLON", 
            DefaultLanguageHighlighterColors.SEMICOLON
        )
        
        val COLON = TextAttributesKey.createTextAttributesKey(
            "COLON", 
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
    }
    
    override fun getHighlightingLexer(): Lexer = WaspLexer()
    
    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when (tokenType) {
            Token.KEYWORD -> arrayOf(KEYWORD)
            Token.STRING -> arrayOf(STRING)
            Token.NUMBER -> arrayOf(NUMBER)
            Token.COMMENT -> arrayOf(COMMENT)
            Token.IDENTIFIER -> arrayOf(IDENTIFIER)
            Token.TYPE -> arrayOf(TYPE)
            Token.OPERATOR -> arrayOf(OPERATOR)
            Token.LBRACE, Token.RBRACE -> arrayOf(BRACES)
            Token.LBRACKET, Token.RBRACKET -> arrayOf(BRACKETS)
            Token.LPAREN, Token.RPAREN -> arrayOf(PARENTHESES)
            Token.COMMA -> arrayOf(COMMA)
            Token.DOT -> arrayOf(DOT)
            Token.SEMICOLON -> arrayOf(SEMICOLON)
            Token.COLON -> arrayOf(COLON)
            TokenType.BAD_CHARACTER -> arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "BAD_CHARACTER", 
                    DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE
                )
            )
            else -> emptyArray()
        }
    }
}
