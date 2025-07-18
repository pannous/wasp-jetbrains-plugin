package com.pannous.wasp

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class WaspColorSettingsPage : ColorSettingsPage {
    
    private val attributesDescriptors = arrayOf(
        AttributesDescriptor("Keyword", WaspSyntaxHighlighter.KEYWORD),
        AttributesDescriptor("Type", WaspSyntaxHighlighter.TYPE),
        AttributesDescriptor("Function Call", WaspSyntaxHighlighter.FUNCTION_CALL),
        AttributesDescriptor("String", WaspSyntaxHighlighter.STRING),
        AttributesDescriptor("Number", WaspSyntaxHighlighter.NUMBER),
        AttributesDescriptor("Comment", WaspSyntaxHighlighter.COMMENT),
        AttributesDescriptor("Identifier", WaspSyntaxHighlighter.IDENTIFIER),
        AttributesDescriptor("Operator", WaspSyntaxHighlighter.OPERATOR),
        AttributesDescriptor("Braces", WaspSyntaxHighlighter.BRACES),
        AttributesDescriptor("Brackets", WaspSyntaxHighlighter.BRACKETS),
        AttributesDescriptor("Parentheses", WaspSyntaxHighlighter.PARENTHESES),
        AttributesDescriptor("Comma", WaspSyntaxHighlighter.COMMA),
        AttributesDescriptor("Dot", WaspSyntaxHighlighter.DOT),
        AttributesDescriptor("Semicolon", WaspSyntaxHighlighter.SEMICOLON),
        AttributesDescriptor("Colon", WaspSyntaxHighlighter.COLON)
    )
    
    override fun getIcon(): Icon? = WaspIcons.FILE
    
    override fun getHighlighter(): SyntaxHighlighter = WaspSyntaxHighlighter()
    
    override fun getDemoText(): String = """
        // Wasp Language Sample
        
        /* Types */
        int x = 42
        string name = "Hello World"
        array numbers = [1, 2, 3]
        map data = {"key": "value"}
        float64 pi = 3.14159
        bool flag = true
        
        /* Function calls */
        print("Positive")
        console.log("Debug message")
        calculate(x, pi)
        result = max(10, 20)
        
        /* Functions */
        fun calculate(a: int, b: real) -> float64 {
            return a + b * 2.0
        }
        
        /* Control flow */
        if (x > 0) {
            print("Positive")
        } else {
            print("Non-positive")
        }
        
        /* Collections */
        for item in numbers {
            print(item)
        }
        
        /* Arithmetic */
        result = 2 + 3 * 4
        """.trimIndent()
    
    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null
    
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = attributesDescriptors
    
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    
    override fun getDisplayName(): String = "Wasp"
}