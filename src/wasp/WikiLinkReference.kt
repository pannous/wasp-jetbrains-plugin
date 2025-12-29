package wasp

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

/**
 * Reference for wiki-style links [[other]] that resolve to other.md files
 */
class WikiLinkReference(element: PsiElement, private val linkText: String, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    override fun resolve(): PsiElement? {
        val results = multiResolve(false)
        return if (results.size == 1) results[0].element else null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = myElement.project
        val fileName = "$linkText.md"

        // Search in the same directory first
        val containingFile = myElement.containingFile?.originalFile
        val containingDir = containingFile?.parent

        val results = mutableListOf<ResolveResult>()

        // Try to find in the same directory
        containingDir?.findFile(fileName)?.let { file ->
            results.add(PsiElementResolveResult(file))
        }

        // If not found in same directory, search globally
        if (results.isEmpty()) {
            val files = FilenameIndex.getFilesByName(
                project,
                fileName,
                GlobalSearchScope.projectScope(project)
            )
            results.addAll(files.map { PsiElementResolveResult(it) })
        }

        return results.toTypedArray()
    }

    override fun getVariants(): Array<Any> {
        // Could provide code completion variants here
        return emptyArray()
    }
}
