package wasp

import org.junit.Test
import org.junit.Assert.*

class WaspElementTypesTest {

    @Test
    fun testAllElementTypesNotNull() {
        assertNotNull(WaspElementTypes.KEYWORD_STATEMENT)
        assertNotNull(WaspElementTypes.IDENTIFIER_STATEMENT)
        assertNotNull(WaspElementTypes.TYPE_STATEMENT)
        assertNotNull(WaspElementTypes.VARIABLE_DECLARATION)
        assertNotNull(WaspElementTypes.FUNCTION_CALL)
        assertNotNull(WaspElementTypes.ARGUMENT_LIST)
        assertNotNull(WaspElementTypes.STRING_LITERAL)
        assertNotNull(WaspElementTypes.NUMBER_LITERAL)
        assertNotNull(WaspElementTypes.ARRAY_LITERAL)
        assertNotNull(WaspElementTypes.MAP_LITERAL)
        assertNotNull(WaspElementTypes.KEY_VALUE_PAIR)
        assertNotNull(WaspElementTypes.ARITHMETIC_EXPRESSION)
        assertNotNull(WaspElementTypes.BLOCK)
        assertNotNull(WaspElementTypes.PAREN_EXPRESSION)
        assertNotNull(WaspElementTypes.LIST_EXPRESSION)
    }

    @Test
    fun testElementTypeNames() {
        assertEquals("WASP_KEYWORD_STATEMENT", WaspElementTypes.KEYWORD_STATEMENT.toString())
        assertEquals("WASP_IDENTIFIER_STATEMENT", WaspElementTypes.IDENTIFIER_STATEMENT.toString())
        assertEquals("WASP_TYPE_STATEMENT", WaspElementTypes.TYPE_STATEMENT.toString())
        assertEquals("WASP_VARIABLE_DECLARATION", WaspElementTypes.VARIABLE_DECLARATION.toString())
        assertEquals("WASP_FUNCTION_CALL", WaspElementTypes.FUNCTION_CALL.toString())
        assertEquals("WASP_ARGUMENT_LIST", WaspElementTypes.ARGUMENT_LIST.toString())
        assertEquals("WASP_STRING_LITERAL", WaspElementTypes.STRING_LITERAL.toString())
        assertEquals("WASP_NUMBER_LITERAL", WaspElementTypes.NUMBER_LITERAL.toString())
        assertEquals("WASP_ARRAY_LITERAL", WaspElementTypes.ARRAY_LITERAL.toString())
        assertEquals("WASP_MAP_LITERAL", WaspElementTypes.MAP_LITERAL.toString())
        assertEquals("WASP_KEY_VALUE_PAIR", WaspElementTypes.KEY_VALUE_PAIR.toString())
        assertEquals("WASP_ARITHMETIC_EXPRESSION", WaspElementTypes.ARITHMETIC_EXPRESSION.toString())
        assertEquals("WASP_BLOCK", WaspElementTypes.BLOCK.toString())
        assertEquals("WASP_PAREN_EXPRESSION", WaspElementTypes.PAREN_EXPRESSION.toString())
        assertEquals("WASP_LIST_EXPRESSION", WaspElementTypes.LIST_EXPRESSION.toString())
    }

    @Test
    fun testElementTypeLanguage() {
        assertEquals(WaspLanguage.INSTANCE, WaspElementTypes.KEYWORD_STATEMENT.language)
        assertEquals(WaspLanguage.INSTANCE, WaspElementTypes.FUNCTION_CALL.language)
        assertEquals(WaspLanguage.INSTANCE, WaspElementTypes.STRING_LITERAL.language)
        assertEquals(WaspLanguage.INSTANCE, WaspElementTypes.ARRAY_LITERAL.language)
        assertEquals(WaspLanguage.INSTANCE, WaspElementTypes.MAP_LITERAL.language)
    }
}