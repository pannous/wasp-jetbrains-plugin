package wasp

import org.junit.Test
import org.junit.Assert.*

class TokenTest {

    @Test
    fun testAllTokensNotNull() {
        assertNotNull(Token.KEYWORD)
        assertNotNull(Token.IDENTIFIER)
        assertNotNull(Token.TYPE)
        assertNotNull(Token.STRING)
        assertNotNull(Token.NUMBER)
        assertNotNull(Token.COMMENT)
        assertNotNull(Token.OPERATOR)
        assertNotNull(Token.NEWLINE)
        assertNotNull(Token.LBRACE)
        assertNotNull(Token.RBRACE)
        assertNotNull(Token.LPAREN)
        assertNotNull(Token.RPAREN)
        assertNotNull(Token.LBRACKET)
        assertNotNull(Token.RBRACKET)
        assertNotNull(Token.COMMA)
        assertNotNull(Token.COLON)
        assertNotNull(Token.SEMICOLON)
        assertNotNull(Token.DOT)
    }

    @Test
    fun testTokenNames() {
        assertEquals("WASP_KEYWORD", Token.KEYWORD.toString())
        assertEquals("WASP_IDENTIFIER", Token.IDENTIFIER.toString())
        assertEquals("WASP_TYPE", Token.TYPE.toString())
        assertEquals("WASP_STRING", Token.STRING.toString())
        assertEquals("WASP_NUMBER", Token.NUMBER.toString())
        assertEquals("WASP_COMMENT", Token.COMMENT.toString())
        assertEquals("WASP_OPERATOR", Token.OPERATOR.toString())
        assertEquals("WASP_NEWLINE", Token.NEWLINE.toString())
        assertEquals("WASP_LBRACE", Token.LBRACE.toString())
        assertEquals("WASP_RBRACE", Token.RBRACE.toString())
        assertEquals("WASP_LPAREN", Token.LPAREN.toString())
        assertEquals("WASP_RPAREN", Token.RPAREN.toString())
        assertEquals("WASP_LBRACKET", Token.LBRACKET.toString())
        assertEquals("WASP_RBRACKET", Token.RBRACKET.toString())
        assertEquals("WASP_COMMA", Token.COMMA.toString())
        assertEquals("WASP_COLON", Token.COLON.toString())
        assertEquals("WASP_SEMICOLON", Token.SEMICOLON.toString())
        assertEquals("WASP_DOT", Token.DOT.toString())
    }

    @Test
    fun testTokenLanguage() {
        assertEquals(WaspLanguage.INSTANCE, Token.KEYWORD.language)
        assertEquals(WaspLanguage.INSTANCE, Token.IDENTIFIER.language)
        assertEquals(WaspLanguage.INSTANCE, Token.STRING.language)
        assertEquals(WaspLanguage.INSTANCE, Token.NUMBER.language)
        assertEquals(WaspLanguage.INSTANCE, Token.LBRACE.language)
        assertEquals(WaspLanguage.INSTANCE, Token.RBRACE.language)
        assertEquals(WaspLanguage.INSTANCE, Token.LPAREN.language)
        assertEquals(WaspLanguage.INSTANCE, Token.RPAREN.language)
    }

    @Test
    fun testDelimiterTokensIntegrity() {
        // Test that all delimiter tokens are properly defined
        val delimiterTokens = listOf(
            Token.LBRACE, Token.RBRACE,
            Token.LPAREN, Token.RPAREN,
            Token.LBRACKET, Token.RBRACKET,
            Token.COMMA, Token.COLON, Token.SEMICOLON, Token.DOT
        )
        
        delimiterTokens.forEach { token ->
            assertNotNull("Token $token should not be null", token)
            assertEquals("Token $token should have WaspLanguage", WaspLanguage.INSTANCE, token.language)
        }
    }

    @Test
    fun testLiteralTokensIntegrity() {
        // Test that all literal tokens are properly defined
        val literalTokens = listOf(
            Token.STRING, Token.NUMBER, Token.IDENTIFIER
        )
        
        literalTokens.forEach { token ->
            assertNotNull("Token $token should not be null", token)
            assertEquals("Token $token should have WaspLanguage", WaspLanguage.INSTANCE, token.language)
        }
    }
}