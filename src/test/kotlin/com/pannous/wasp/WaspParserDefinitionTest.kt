package com.pannous.wasp

import com.intellij.openapi.project.Project
import com.intellij.psi.TokenType
import com.intellij.testFramework.LightPlatformTestCase
import org.junit.Test

class WaspParserDefinitionTest : LightPlatformTestCase() {

    private val parserDefinition = WaspParserDefinition()

    @Test
    fun testCreateLexer() {
        val lexer = parserDefinition.createLexer(project)
        assertNotNull(lexer)
        assertTrue(lexer is WaspLexer)
    }

    @Test
    fun testCreateParser() {
        val parser = parserDefinition.createParser(project)
        assertNotNull(parser)
        assertTrue(parser is WaspParser)
    }

    @Test
    fun testGetFileNodeType() {
        val fileNodeType = parserDefinition.fileNodeType
        assertNotNull(fileNodeType)
        assertEquals("WASP_FILE", fileNodeType.toString())
    }

    @Test
    fun testGetWhitespaceTokens() {
        val whitespaceTokens = parserDefinition.whitespaceTokens
        assertNotNull(whitespaceTokens)
        assertTrue(whitespaceTokens.contains(TokenType.WHITE_SPACE))
    }

    @Test
    fun testGetCommentTokens() {
        val commentTokens = parserDefinition.commentTokens
        assertNotNull(commentTokens)
        assertTrue(commentTokens.contains(Token.COMMENT))
    }

    @Test
    fun testGetStringLiteralElements() {
        val stringTokens = parserDefinition.stringLiteralElements
        assertNotNull(stringTokens)
        assertTrue(stringTokens.contains(Token.STRING))
    }

    @Test
    fun testStaticFields() {
        assertNotNull(WaspParserDefinition.WHITE_SPACES)
        assertNotNull(WaspParserDefinition.COMMENTS)
        assertNotNull(WaspParserDefinition.STRINGS)
        assertNotNull(WaspParserDefinition.FILE)
    }

    @Test
    fun testTokenSetContents() {
        assertTrue(WaspParserDefinition.WHITE_SPACES.contains(TokenType.WHITE_SPACE))
        assertTrue(WaspParserDefinition.COMMENTS.contains(Token.COMMENT))
        assertTrue(WaspParserDefinition.STRINGS.contains(Token.STRING))
    }
}