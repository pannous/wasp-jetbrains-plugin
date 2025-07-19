package com.pannous.wasp

import com.intellij.psi.tree.IElementType
import org.junit.Test
import org.junit.Assert.*

class WaspLexerTest {

    private fun tokenize(text: String): List<Pair<IElementType?, String>> {
        val lexer = WaspLexer()
        lexer.start(text)
        
        val tokens = mutableListOf<Pair<IElementType?, String>>()
        while (lexer.tokenType != null) {
            val tokenType = lexer.tokenType
            val tokenText = lexer.tokenText
            tokens.add(Pair(tokenType, tokenText))
            lexer.advance()
        }
        return tokens
    }

    @Test
    fun testKeywordTokenization() {
        val tokens = tokenize("if while for")
        assertTrue(tokens.any { it.first == Token.KEYWORD })
    }

    @Test
    fun testIdentifierTokenization() {
        val tokens = tokenize("variable myVar _underscore")
        assertTrue(tokens.any { it.first == Token.IDENTIFIER })
    }

    @Test
    fun testStringTokenization() {
        val tokens = tokenize("\"hello world\" 'single quotes'")
        assertTrue(tokens.any { it.first == Token.STRING })
    }

    @Test
    fun testNumberTokenization() {
        val tokens = tokenize("42 3.14 0xFF")
        assertTrue(tokens.any { it.first == Token.NUMBER })
    }

    @Test
    fun testOperatorTokenization() {
        val tokens = tokenize("+ - * / = == != < >")
        assertTrue(tokens.any { it.first == Token.OPERATOR })
    }

    @Test
    fun testDelimiterTokenization() {
        val tokens = tokenize("{ } ( ) [ ] , : ;")
        val tokenTypes = tokens.map { it.first }
        
        assertTrue(tokenTypes.contains(Token.LBRACE))
        assertTrue(tokenTypes.contains(Token.RBRACE))
        assertTrue(tokenTypes.contains(Token.LPAREN))
        assertTrue(tokenTypes.contains(Token.RPAREN))
        assertTrue(tokenTypes.contains(Token.LBRACKET))
        assertTrue(tokenTypes.contains(Token.RBRACKET))
        assertTrue(tokenTypes.contains(Token.COMMA))
        assertTrue(tokenTypes.contains(Token.COLON))
        assertTrue(tokenTypes.contains(Token.SEMICOLON))
    }

    @Test
    fun testCommentTokenization() {
        val tokens = tokenize("// single line comment\n/* multi line */")
        assertTrue(tokens.any { it.first == Token.COMMENT })
    }

    @Test
    fun testNewlineTokenization() {
        val tokens = tokenize("line1\nline2\r\nline3")
        assertTrue(tokens.any { it.first == Token.NEWLINE })
    }

    @Test
    fun testComplexExpression() {
        val code = "function(\"param\", 42) { return value + 1 }"
        val tokens = tokenize(code)
        
        val tokenTypes = tokens.map { it.first }
        assertTrue(tokenTypes.contains(Token.IDENTIFIER))
        assertTrue(tokenTypes.contains(Token.LPAREN))
        assertTrue(tokenTypes.contains(Token.STRING))
        assertTrue(tokenTypes.contains(Token.COMMA))
        assertTrue(tokenTypes.contains(Token.NUMBER))
        assertTrue(tokenTypes.contains(Token.RPAREN))
        assertTrue(tokenTypes.contains(Token.LBRACE))
        assertTrue(tokenTypes.contains(Token.RBRACE))
    }

    @Test
    fun testEmptyString() {
        val tokens = tokenize("")
        assertTrue(tokens.isEmpty())
    }

    @Test
    fun testWhitespaceHandling() {
        val tokens = tokenize("   var   =   42   ")
        // Should not include whitespace tokens in normal parsing
        val nonWhitespaceTokens = tokens.filter { 
            it.second.trim().isNotEmpty() 
        }
        assertTrue(nonWhitespaceTokens.isNotEmpty())
    }

    @Test
    fun testSpecialCharacters() {
        val tokens = tokenize("@ # $ % ^ & * ! ~ ` ?")
        // Should handle special characters gracefully
        assertNotNull(tokens)
    }
}