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
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiPlainText::class.java),
            WikiLinkReferenceProvider()
        )
    }
}

class WikiLinkReferenceProvider : PsiReferenceProvider() {
    companion object {
        private val WIKI_LINK_PATTERN = Regex("""\[\[([^\]]+)\]\]""")
    }

    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        val text = element.text
        val references = mutableListOf<PsiReference>()

        // Find all [[...]] patterns in the text
        WIKI_LINK_PATTERN.findAll(text).forEach { matchResult ->
            val linkText = matchResult.groupValues[1]
            val startOffset = matchResult.range.first
            val endOffset = matchResult.range.last + 1

            // Create a text range for just the link name (excluding [[ and ]])
            val textRange = TextRange(startOffset + 2, endOffset - 2)

            references.add(WikiLinkReference(element, linkText, textRange))
        }

        return references.toTypedArray()
    }
}
