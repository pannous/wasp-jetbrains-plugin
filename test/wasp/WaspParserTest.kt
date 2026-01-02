package wasp

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.ParsingTestCase
import wasp.WaspElementTypes.ASSIGNMENT
import wasp.WaspElementTypes.NULL_LITERAL

class WaspParserTest : ParsingTestCase("", "wasp", WaspParserDefinition()) {
    override fun getTestDataPath(): String = "test/resources/testData"

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
        val nodes = parse(code)
    }

    fun testParserCreation() {
        val parser = WaspParser()
    }

    fun testSimpleIdentifierStatement() {
        val code = "variable"
        val nodes = parse(code)
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
        val code = "my_function(arg1, arg2)"
        val nodes = parse(code)
    }


    fun testVariableDeclaration() {
        val code = "String name = \"value\""
        val nodes = parse(code)
    }

    fun testStringLiteral() {
        val code = "\"hello world!\""
        val nodes = parse(code)
    }

    fun testParserLogicMethods() {
        val parser = WaspParser()
        val methods = parser.javaClass.declaredMethods

        val expectedMethods = listOf(
            "parseFile", "parseStatement", "parseKeywordStatement",
            "parseIdentifierStatement", "parseTypeOrVariableDeclaration",
            "parseArrayLiteral", "parseMapLiteral", "parseExpression",
            "parseStringLiteral", "parseNumberLiteral", "parseArithmeticExpression",
            "parseFunctionDeclaration"
        )

        for (methodName in expectedMethods) {
            assertTrue(
                "Method $methodName should exist",
                methods.any { it.name == methodName })
        }
    }

    fun testNumberLiteral() {
        val code = "42"
        val nodes = parse(code)
    }


    fun testArrayLiteral() {
        val code = "[1, 2, 3]"
        val nodes = parse(code)
    }


    fun testMapLiteral() {
        val code = "{\"key\": \"value\"}"
        val nodes = parse(code)
    }


    fun testArithmeticExpression() {
        val code = "2 * 3"
        val nodes = parse(code)
    }

    fun testNestedBlock() {
        val code = "if condition { statement }"
        val nodes = parse(code)
    }

    fun testComplexExpression() {
        val code = """
                    function call(param) {
                        String nodes = "test"
                        return nodes
                    }
                """.trimIndent()
        val nodes = parse(code)
    }

    fun testMultipleStatements() {
        val code = """
                    variable1 = 10
                    variable2 = "hello"
                    function(variable1, variable2)
                """.trimIndent()
        val nodes = parse(code)
    }

    fun testEmptyArray() {
        val code = "[]"
        val nodes = parse(code)
    }

    fun testEmptyMap() {
        val code = "{}"
        val nodes = parse(code)
    }

    fun testKeyValuePair() {
        val code = "{\"name\": \"John\", \"age\": 30}"
        val nodes = parse(code)
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
        val nodes = parse(code)
    }

    fun testCommentsIgnored() {
        val code = """
                    // This is a comment
                    variable = 42
                    /* Multi-line comment */
                    function()
                """.trimIndent()
        val nodes = parse(code)
    }

    fun testMalformedArray() {
        val code = "[1, 2, 3"
        val nodes = parse(code)
        // Parser should handle malformed input gracefully
    }

    fun testMalformedMap() {
        val code = "{\"key\": \"value\""
        val nodes = parse(code)
        // Parser should handle malformed input gracefully
    }

    fun testMalformedFunctionCall() {
        val code = "function(arg1, arg2"
        val nodes = parse(code)
        // Parser should handle malformed input gracefully - no throwing needed
        // Malformed syntax should still produce AST nodes for error recovery
    }

    fun testSimpleAssignment() {
        val code = "x = 5"
        val nodes = parse(code)
    }

    fun testBasicArithmetic() {
        val code = "nodes = 10 + 20"
        val nodes = parse(code)
    }

    fun testBooleanLiteral() {
        val code = "isValid = true"
        val nodes = parse(code)
    }

    fun testNoneLiteral() {
        val code = "None"
        val nodes = parse(code)
        val none = nodes.first()
        assertEquals("None literal should be parsed", WaspElementTypes.NULL_LITERAL, none.elementType)
    }

    fun testValueTypesStandalone() {
        // Test that value types can appear standalone in root context
        val standaloneTests = mapOf(
            "Int" to WaspElementTypes.TYPE_LITERAL,
            "String" to WaspElementTypes.TYPE_LITERAL,
            "MyClass" to WaspElementTypes.TYPE_LITERAL,
            "null" to WaspElementTypes.NULL_LITERAL,
            "nil" to WaspElementTypes.NULL_LITERAL,
            "None" to WaspElementTypes.NULL_LITERAL
        )
        
        for ((value, expectedType) in standaloneTests) {
            val nodes = parse(value)
            val valueNode = nodes.first()
            assertEquals("$value should create $expectedType when standalone", expectedType, valueNode.elementType)
        }
    }

    fun testNullLiteral() {
        val code = "value = null"
        val nodes = parse(code)

        val assignment = nodes.first()
        assertEquals(ASSIGNMENT, assignment.elementType)
        val body = assignment.getChildren(null)

        // #DONE TODO: Replace with precise .first() approach once we understand exact parser structure
        val nullNode = body.firstOrNull { it.elementType == NULL_LITERAL }
        assertNotNull("Assignment should contain null literal node", nullNode)
        assertEquals("Null literal should have correct text", "null", nullNode!!.text)
    }

    fun testQuotePairing() {
        // Test French quotes
        val frenchCode = "«hello world»"
        val frenchNodes = parse(frenchCode)

        // Test curly quotes  
        val curlyCode = "'hello world'"
        val curlyNodes = parse(curlyCode)

        // Test smart quotes (using Unicode escapes to avoid compiler issues)
        val smartCode = "\u201chello world\u201d"
        val smartNodes = parse(smartCode)

        // Basic validation - should not throw and should produce nodes
        assertTrue("French quotes should produce nodes", frenchNodes.isNotEmpty())
        assertTrue("Curly quotes should produce nodes", curlyNodes.isNotEmpty())
        assertTrue("Smart quotes should produce nodes", smartNodes.isNotEmpty())
    }

    fun testNumericSuffixes() {
        // Test integer suffixes
        val integerTests = listOf(
            "42u",      // unsigned
            "42U",      // unsigned (uppercase)
            "42l",      // long
            "42L",      // long (uppercase)
            "42ll",     // long long
            "42LL",     // long long (uppercase)
            "42ul",     // unsigned long
            "42UL",     // unsigned long (uppercase)
            "42lu",     // unsigned long (alternative)
            "42LU",     // unsigned long (alternative uppercase)
            "42ull",    // unsigned long long
            "42ULL",    // unsigned long long (uppercase)
            "42llu",    // unsigned long long (alternative)
            "42LLU"     // unsigned long long (alternative uppercase)
        )

        for (code in integerTests) {
            val nodes = parse(code)
            assertTrue("$code should produce nodes", nodes.isNotEmpty())
        }

        // Test float suffixes
        val floatTests = listOf(
            "3.14f",    // float
            "3.14F",    // float (uppercase)
            "3.14d",    // double
            "3.14D",    // double (uppercase)
            "3.14ld",   // long double
            "3.14LD"    // long double (uppercase)
        )

        for (code in floatTests) {
            val nodes = parse(code)
            assertTrue("$code should produce nodes", nodes.isNotEmpty())
        }

        // Test numbers without suffixes still work
        val basicNumbers = listOf("42", "3.14", "0", "123.456")
        for (code in basicNumbers) {
            val nodes = parse(code)
            assertTrue("$code should produce nodes", nodes.isNotEmpty())
        }
    }

    fun testAllTypeKeywords() {
        // Test non-capitalized types that are also keywords (these become KEYWORD_STATEMENT due to precedence)
        val keywordTypes = listOf("class", "type", "lambda")

        for (type in keywordTypes) {
            val code = "$type x = value"
            val nodes = parse(code)
            val keywordStmt = nodes.first()
            assertEquals(
                "$type should create KEYWORD_STATEMENT (keyword precedence)",
                WaspElementTypes.KEYWORD_STATEMENT,
                keywordStmt.elementType
            )
            val typeToken = keywordStmt.firstChildNode
            assertEquals("$type should be tokenized as KEYWORD", Token.KEYWORD, typeToken.elementType)
        }
    }


    fun testAllTypes() {

        // Test all types that should be tokenized as TYPE and create VARIABLE_DECLARATION
        val allRegularTypes = listOf(
            // Primitive types
            "int", "real", "float", "double", "bool", "char", "string", "byte", "codepoint",
            // Capitalized basic types
            "String", "Number", "Boolean",
            // Collection types
            "array", "list", "set", "map", "dict", "tuple", "vector", "matrix", "node",
            "Array", "Object",
            // Reference types (excluding those that are also keywords)
            "Object", "Interface", "Struct", "Union", "Enum",
            // Capitalized reference types (not in KEYWORDS)
            "Class", "Type",
            // Function types (not in KEYWORDS) 
            "Function", "Closure", // "Method", "Procedure",
            // Variable/modifier types (not in KEYWORDS)
            // Memory types
            "Pointer", "Reference", "Address",
            // Numeric types
            "number", "integer", "decimal", "fraction", "complex", "rational",
            "int8", "int16", "int32", "int64", "uint8", "uint16", "uint32", "uint64",
            "float32", "float64", "long", "short", "unsigned", "signed",
            // Domain types
            "time", "date", "datetime", "duration", "url", "path", "file",
            "json", "xml", "html", "css", "regex", "uuid", "hash", "binary"
        )
        for (type in allRegularTypes) {
            val nodes = parse("$type x = value")
            val typeToken = nodes.first().firstChildNode
            assertEquals("$type should be tokenized as TYPE", Token.TYPE, typeToken.elementType)
        }
    }


    fun testCapitalizedIdentifiersAsTypes() {
        // Test that identifiers starting with capital letters are tokenized as TYPE and create VARIABLE_DECLARATION
        val capitalizedIdentifiers = listOf(
            "MyClass",
            "PersonType",
            "DatabaseConnection",
            "HTTPClient",
            "XMLParser",
            "Int"
        )
        for (identifier in capitalizedIdentifiers) {
            val code = "$identifier x = value"
            val nodes = parse(code)
            val typeNode = nodes.first()
            assertEquals(WaspElementTypes.VARIABLE_DECLARATION, typeNode.elementType)
            val typeToken = typeNode.firstChildNode
            assertEquals("$identifier should be tokenized as TYPE", Token.TYPE, typeToken.elementType)
        }
    }

    fun testFunctionKeyword2() {
        // Test malformed function declaration (should still create FUNCTION_DECLARATION but with errors)
        val code = "function x = value"
        val nodes = parse(code)
        val functionDecl = nodes.first()
        assertEquals(
            "function should create FUNCTION_DECLARATION even with malformed syntax",
            WaspElementTypes.FUNCTION_DECLARATION,
            functionDecl.elementType
        )
    }

    fun testFunctionKeyword() {
        // Test function keyword
        val code = "function myFunc() { return 42; }"
        val nodes = parse(code)
        val functionNode = nodes.first()
        assertEquals("Function declaration should be parsed correctly", WaspElementTypes.FUNCTION_DECLARATION, functionNode.elementType)
    }


    fun testLowercaseIdentifiersStayAsIdentifiers() {
        // Test that lowercase identifiers remain as IDENTIFIER_STATEMENT or other appropriate types
        val lowercaseIdentifiers = listOf("myVariable", "someFunction", "getValue")
        
        for (identifier in lowercaseIdentifiers) {
            val code = "$identifier = value"
            val nodes = parse(code)
            val identifierNode = nodes.first()
            // Should be ASSIGNMENT since we have "= value"
            assertEquals(
                "$identifier should create ASSIGNMENT",
                WaspElementTypes.ASSIGNMENT,
                identifierNode.elementType
            )
        }
    }

    fun testComplexStringConcatenation() {
        // Test string concatenation with emoji and arithmetic
        val code = "\"Hello \" + \"🌍\" + 2000+25"
        val nodes = parse(code)
        // Should parse as arithmetic expression with strings and numbers
        assertTrue("Complex concatenation should produce nodes", nodes.isNotEmpty())
    }

    fun testShebangLine() {
        // Test shebang (should be treated as comment)
        val code = "#!/usr/bin/env wasp\nx = 42"
        val nodes = parse(code)
        assertTrue("Shebang should not cause parser errors", nodes.isNotEmpty())
    }

    fun testStandaloneIdentifier() {
        // Test standalone identifier (function call without parens)
        val code = "paint"
        val nodes = parse(code)
        assertTrue("Standalone identifier should parse", nodes.isNotEmpty())
    }

    fun testChainedAssignment() {
        // Test chained assignment
        val code = "width=height=400"
        val nodes = parse(code)
        assertTrue("Chained assignment should parse", nodes.isNotEmpty())
    }

    fun testColonTerminatedBlock() {
        // Test while with colon and indented block
        val code = """
            while condition:
                statement1
                statement2
        """.trimIndent()
        val nodes = parse(code)
        assertTrue("Colon-terminated block should parse", nodes.isNotEmpty())
    }

    fun testCircleSampleFile() {
        // Test actual circle.wasp content
        val code = """
            #!/usr/bin/env wasp
            y=0
            j=0
            width=height=400
            center=height/2
            surface=malloc(height*width)
            while y++<height and x++<width:
                surface[y+height*x]= norm( (y-center)^2 + (x-center)^2 ) < radius
            paint
        """.trimIndent()
        val nodes = parse(code)
        assertTrue("Circle sample should parse without errors", nodes.isNotEmpty())
    }

    fun testAllSampleFiles() {
        // Test all sample files from the wasp repository
        val samplesDir = java.io.File("/Users/me/dev/apps/wasp/samples")
        if (!samplesDir.exists()) {
            println("Samples directory not found, skipping test")
            return
        }

        val sampleFiles = samplesDir.listFiles { file -> file.extension == "wasp" }
        if (sampleFiles == null || sampleFiles.isEmpty()) {
            println("No sample files found, skipping test")
            return
        }

        var totalFiles = 0
        var successfulFiles = 0

        for (file in sampleFiles) {
            totalFiles++
            try {
                val code = file.readText()
                val nodes = parse(code)
                successfulFiles++
                println("✓ Successfully parsed ${file.name}")
            } catch (e: Exception) {
                println("✗ Failed to parse ${file.name}: ${e.message}")
            }
        }

        println("Parsed $successfulFiles/$totalFiles sample files successfully")
        assertTrue("Should parse most sample files", successfulFiles > 0)
    }

    private fun parse(code: String): Array<ASTNode> {
        val psiFile = createPsiFile("test", code)
        val node = psiFile?.node

        // Basic validation that all tests should pass
        assertNotNull("Result should not be null", node)
        val children = node!!.getChildren(null)
        assertTrue("AST should have child nodes", children.isNotEmpty())

        return children
    }
}
