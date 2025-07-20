package wasp

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class WaspFoldingBuilder : FoldingBuilderEx(), DumbAware {
    
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        
        // Find all foldable elements in the PSI tree
        PsiTreeUtil.findChildrenOfAnyType(root, PsiElement::class.java).forEach { element ->
            when {
                // Fold blocks (elements with braces)
                isBlockElement(element) -> {
                    val range = getBlockFoldingRange(element)
                    if (range != null && range.length > 2) {
                        descriptors.add(FoldingDescriptor(element.node, range))
                    }
                }
                
                // Fold multi-line comments
                isMultiLineComment(element) -> {
                    val range = element.textRange
                    if (range.length > 4) { // Minimum length for /* */
                        descriptors.add(FoldingDescriptor(element.node, range))
                    }
                }
                
                // Fold control structures (if/else blocks)
                isControlStructure(element) -> {
                    val range = getControlStructureFoldingRange(element)
                    if (range != null && range.length > 10) {
                        descriptors.add(FoldingDescriptor(element.node, range))
                    }
                }
            }
        }
        
        return descriptors.toTypedArray()
    }
    
    private fun isBlockElement(element: PsiElement): Boolean {
        val text = element.text
        return text.contains("{") && text.contains("}")
    }
    
    private fun isMultiLineComment(element: PsiElement): Boolean {
        val text = element.text
        return text.startsWith("/*") && text.endsWith("*/") && text.contains("\n")
    }
    
    private fun isControlStructure(element: PsiElement): Boolean {
        val text = element.text.trim()
        return text.startsWith("if ") || text.startsWith("else") || 
               text.startsWith("for ") || text.startsWith("while ")
    }
    
    private fun getBlockFoldingRange(element: PsiElement): TextRange? {
        val text = element.text
        val startBrace = text.indexOf('{')
        val endBrace = text.lastIndexOf('}')
        
        if (startBrace == -1 || endBrace == -1 || startBrace >= endBrace) {
            return null
        }
        
        // Check if this is a single-line block (opening and closing braces on same line)
        val textUpToStart = text.substring(0, startBrace)
        val textBetweenBraces = text.substring(startBrace, endBrace + 1)
        
        val startLineBreaks = textUpToStart.count { it == '\n' }
        val betweenLineBreaks = textBetweenBraces.count { it == '\n' }
        
        // If opening and closing braces are on the same line, don't fold
        if (betweenLineBreaks == 0) {
            return null
        }
        
        // Find the exact position of the closing brace to avoid including trailing content
        val lines = text.split('\n')
        var currentPos = 0
        var braceCount = 0
        var actualEndBrace = -1
        
        for (char in text) {
            if (char == '{') {
                braceCount++
            } else if (char == '}') {
                braceCount--
                if (braceCount == 0 && currentPos >= startBrace) {
                    actualEndBrace = currentPos
                    break
                }
            }
            currentPos++
        }
        
        if (actualEndBrace == -1) {
            return null
        }
        
        val startOffset = element.textRange.startOffset + startBrace
        val endOffset = element.textRange.startOffset + actualEndBrace + 1
        
        return TextRange(startOffset, endOffset)
    }
    
    private fun getControlStructureFoldingRange(element: PsiElement): TextRange? {
        val text = element.text
        val colonIndex = text.indexOf(':')
        
        if (colonIndex == -1) {
            return null
        }
        
        // Find the end of the control structure block
        val lines = text.split('\n')
        var indentLevel = -1
        var endLine = lines.size - 1
        
        for (i in 1 until lines.size) {
            val line = lines[i]
            if (line.trim().isEmpty()) continue
            
            val currentIndent = line.length - line.trimStart().length
            if (indentLevel == -1) {
                indentLevel = currentIndent
            } else if (currentIndent <= indentLevel && line.trim().isNotEmpty()) {
                endLine = i - 1
                break
            }
        }
        
        if (endLine <= 0) {
            return null
        }
        
        val endOffset = element.textRange.startOffset + 
                       lines.take(endLine + 1).joinToString("\n").length
        
        return TextRange(element.textRange.startOffset + colonIndex, endOffset)
    }
    
    override fun getPlaceholderText(node: ASTNode): String? {
        val element = node.psi
        val text = element.text
        
        return when {
            isBlockElement(element) -> {
                val firstLine = text.lines().first().trim()
                if (firstLine.endsWith("{")) {
                    "${firstLine.dropLast(1).trim()} { ... }"
                } else {
                    "{ ... }"
                }
            }
            
            isMultiLineComment(element) -> "/* ... */"
            
            isControlStructure(element) -> {
                val firstLine = text.lines().first().trim()
                if (firstLine.contains(":")) {
                    "${firstLine.substringBefore(":")}:..."
                } else {
                    "..."
                }
            }
            
            else -> "..."
        }
    }
    
    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        // Don't collapse anything by default - let users choose
        return false
    }
}