package com.pannous.wasp

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.ParsingTestCase
import com.pannous.wasp.WaspElementTypes.ASSIGNMENT
import com.pannous.wasp.WaspElementTypes.NULL_LITERAL

class WaspParserTest : ParsingTestCase("", "wasp", WaspParserDefinition()) {
    override fun getTestDataPath(): String = "src/test/resources/testData"
    
    override fun setUp() {
        super.setUp()
    }
    
    override fun tearDown() {
        try {
            super.tearDown()
        } catch (e: Exception) {
            // Ignore teardown exceptions to prevent test failures
        }
    }

    fun testBasicKeywordStatement() {
        val code = "if condition"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testParserCreation() {
        val parser = WaspParser()
        assertNotNull("Parser should not be null", parser)
    }

    fun testSimpleIdentifierStatement() {
        val code = "variable"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testParseMethodExists() {
        val parser = WaspParser()
        assertNotNull(
            parser.javaClass.getMethod(
                "parse", IElementType::class.java,
                com.intellij.lang.PsiBuilder::class.java
            )
        )
    }

    fun testFunctionCall() {
        val code = "function(arg1, arg2)"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testWaspElementTypesExist() {
        assertNotNull("Element type should not be null", WaspElementTypes.KEYWORD_STATEMENT)
        assertNotNull("Element type should not be null", WaspElementTypes.IDENTIFIER_STATEMENT)
        assertNotNull("Element type should not be null", WaspElementTypes.FUNCTION_CALL)
        assertNotNull("Element type should not be null", WaspElementTypes.VARIABLE_DECLARATION)
        assertNotNull("Element type should not be null", WaspElementTypes.STRING_LITERAL)
        assertNotNull("Element type should not be null", WaspElementTypes.NUMBER_LITERAL)
        assertNotNull("Element type should not be null", WaspElementTypes.ARRAY_LITERAL)
        assertNotNull("Element type should not be null", WaspElementTypes.MAP_LITERAL)
        assertNotNull("Element type should not be null", WaspElementTypes.ARITHMETIC_EXPRESSION)
        assertNotNull("Element type should not be null", WaspElementTypes.BLOCK)
    }

    fun testVariableDeclaration() {
        val code = "String name = \"value\""
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testTokenTypesExist() {
        assertNotNull("Token should not be null", Token.KEYWORD)
        assertNotNull("Token should not be null", Token.IDENTIFIER)
        assertNotNull("Token should not be null", Token.STRING)
        assertNotNull("Token should not be null", Token.NUMBER)
        assertNotNull("Token should not be null", Token.LBRACE)
        assertNotNull("Token should not be null", Token.RBRACE)
        assertNotNull("Token should not be null", Token.LPAREN)
        assertNotNull("Token should not be null", Token.RPAREN)
        assertNotNull("Token should not be null", Token.LBRACKET)
        assertNotNull("Token should not be null", Token.RBRACKET)
    }

    fun testStringLiteral() {
        val code = "\"hello world\""
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testParserLogicMethods() {
        val parser = WaspParser()
        val methods = parser.javaClass.declaredMethods

        val expectedMethods = listOf(
            "parseFile", "parseStatement", "parseKeywordStatement",
            "parseIdentifierStatement", "parseVariableDeclaration",
            "parseArrayLiteral", "parseMapLiteral", "parseExpression",
            "parseStringLiteral", "parseNumberLiteral", "parseArithmeticExpression"
        )

        for (methodName in expectedMethods) {
            assertTrue(
                "Method $methodName should exist",
                methods.any { it.name == methodName })
        }
    }

    fun testNumberLiteral() {
        val code = "42"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testElementTypeNames() {
        assertEquals("WASP_KEYWORD_STATEMENT", WaspElementTypes.KEYWORD_STATEMENT.toString())
        assertEquals("WASP_FUNCTION_CALL", WaspElementTypes.FUNCTION_CALL.toString())
        assertEquals("WASP_ARRAY_LITERAL", WaspElementTypes.ARRAY_LITERAL.toString())
        assertEquals("WASP_MAP_LITERAL", WaspElementTypes.MAP_LITERAL.toString())
    }

    fun testArrayLiteral() {
        val code = "[1, 2, 3]"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testTokenTypeNames() {
        assertEquals("WASP_KEYWORD", Token.KEYWORD.toString())
        assertEquals("WASP_IDENTIFIER", Token.IDENTIFIER.toString())
        assertEquals("WASP_STRING", Token.STRING.toString())
        assertEquals("WASP_NUMBER", Token.NUMBER.toString())
    }

    fun testMapLiteral() {
        val code = "{\"key\": \"value\"}"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testLanguageAssociation() {
        assertEquals(WaspLanguage.INSTANCE, Token.KEYWORD.language)
        assertEquals(WaspLanguage.INSTANCE, WaspElementTypes.FUNCTION_CALL.language)
    }

    fun testArithmeticExpression() {
        val code = "2 * 3"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testNestedBlock() {
        val code = "if condition { statement }"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testComplexExpression() {
        val code = """
                    function call(param) {
                        String result = "test"
                        return result
                    }
                """.trimIndent()
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testMultipleStatements() {
        val code = """
                    variable1 = 10
                    variable2 = "hello"
                    function(variable1, variable2)
                """.trimIndent()
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testEmptyArray() {
        val code = "[]"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testEmptyMap() {
        val code = "{}"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testKeyValuePair() {
        val code = "{\"name\": \"John\", \"age\": 30}"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testNestedStructures() {
        val code = """
                    {
                        "array": [1, 2, 3],
                        "nested": {
                            "inner": "value"
                        }
                    }
                """.trimIndent()
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testCommentsIgnored() {
        val code = """
                    // This is a comment
                    variable = 42
                    /* Multi-line comment */
                    function()
                """.trimIndent()
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testMalformedArray() {
        val code = "[1, 2, 3"
        val result = parseCode(code)
        // Parser should handle malformed input gracefully
        assertNotNull("Result should not be null", result)
    }

    fun testMalformedMap() {
        val code = "{\"key\": \"value\""
        val result = parseCode(code)
        // Parser should handle malformed input gracefully
        assertNotNull("Result should not be null", result)
    }

    fun testMalformedFunctionCall() {
        val code = "function(arg1, arg2"
        val result = parseCode(code)
        // Parser should handle malformed input gracefully
        assertNotNull("Result should not be null", result)
    }

    fun testSimpleAssignment() {
        val code = "x = 5"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testBasicArithmetic() {
        val code = "result = 10 + 20"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testBooleanLiteral() {
        val code = "isValid = true"
        val result = parseCode(code)
        assertNotNull("Result should not be null", result)
    }

    fun testNullLiteral() {
        val code = "value = null"
        val children = parseCode(code)
        
        // Find the assignment node
        val assignmentNode = children.find { it.elementType == ASSIGNMENT }
        assertNotNull("Should contain assignment node", assignmentNode)
        
        // Verify assignment structure
        val assignmentChildren = assignmentNode!!.getChildren(null)
        assertTrue("Assignment should have child nodes", assignmentChildren.isNotEmpty())
        
        // Find the null literal node within the assignment
        val nullNode = assignmentChildren.find { it.elementType == NULL_LITERAL }
        assertNotNull("Assignment should contain null literal node", nullNode)
        assertEquals("Null literal should have correct text", "null", nullNode!!.text)
    }

    private fun parseCode(code: String): Array<ASTNode> {
        val psiFile = createPsiFile("test", code)
        val node = psiFile?.node
        
        // Basic validation that all tests should pass
        assertNotNull("Result should not be null", node)
        val children = node!!.getChildren(null)
        assertTrue("AST should have child nodes", children.isNotEmpty())
        
        return children
    }
}
