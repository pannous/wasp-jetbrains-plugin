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
        assertEquals("KEYWORD_STATEMENT", WaspElementTypes.KEYWORD_STATEMENT.toString())
        assertEquals("IDENTIFIER_STATEMENT", WaspElementTypes.IDENTIFIER_STATEMENT.toString())
        assertEquals("TYPE_STATEMENT", WaspElementTypes.TYPE_STATEMENT.toString())
        assertEquals("VARIABLE_DECLARATION", WaspElementTypes.VARIABLE_DECLARATION.toString())
        assertEquals("FUNCTION_CALL", WaspElementTypes.FUNCTION_CALL.toString())
        assertEquals("ARGUMENT_LIST", WaspElementTypes.ARGUMENT_LIST.toString())
        assertEquals("STRING_LITERAL", WaspElementTypes.STRING_LITERAL.toString())
        assertEquals("NUMBER_LITERAL", WaspElementTypes.NUMBER_LITERAL.toString())
        assertEquals("ARRAY_LITERAL", WaspElementTypes.ARRAY_LITERAL.toString())
        assertEquals("MAP_LITERAL", WaspElementTypes.MAP_LITERAL.toString())
        assertEquals("KEY_VALUE_PAIR", WaspElementTypes.KEY_VALUE_PAIR.toString())
        assertEquals("ARITHMETIC_EXPRESSION", WaspElementTypes.ARITHMETIC_EXPRESSION.toString())
        assertEquals("BLOCK", WaspElementTypes.BLOCK.toString())
        assertEquals("PAREN_EXPRESSION", WaspElementTypes.PAREN_EXPRESSION.toString())
        assertEquals("LIST_EXPRESSION", WaspElementTypes.LIST_EXPRESSION.toString())
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
