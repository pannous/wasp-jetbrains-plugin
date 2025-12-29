package wasp

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

/**
 * Contributes references for wiki-style links [[other]] in markdown files
 * Enables Ctrl+Click navigation from [[other]] to other.md
 */
class WikiLinkReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // Register for all PSI elements to catch wiki links in any context
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(),
            WikiLinkReferenceProvider()
        )
    }
}

class WikiLinkReferenceProvider : PsiReferenceProvider() {
    companion object {
        // Support both [[other]] and {{other}} syntax for debugging
        private val WIKI_LINK_PATTERN = Regex("""(?:\[\[([^\]]+)\]\]|\{\{([^}]+)\}\})""")
    }

    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        // Skip if element is not a leaf (to avoid processing container elements)
        if (element.firstChild != null) {
            return PsiReference.EMPTY_ARRAY
        }

        val text = element.text ?: return PsiReference.EMPTY_ARRAY
        val references = mutableListOf<PsiReference>()

        // Find all [[...]] and {{...}} patterns in the text
        WIKI_LINK_PATTERN.findAll(text).forEach { matchResult ->
            // Get the link text from whichever group matched
            val linkText = matchResult.groupValues[1].ifEmpty { matchResult.groupValues[2] }
            val startOffset = matchResult.range.first
            val endOffset = matchResult.range.last + 1

            // Create a text range for just the link name (excluding [[ ]] or {{ }})
            val textRange = TextRange(startOffset + 2, endOffset - 2)

            references.add(WikiLinkReference(element, linkText, textRange))
        }

        return references.toTypedArray()
    }
}
