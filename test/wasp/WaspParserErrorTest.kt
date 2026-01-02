package wasp

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.ParsingTestCase
import java.io.File

class WaspParserErrorTest : ParsingTestCase("", "wasp", WaspParserDefinition()) {
    override fun getTestDataPath(): String = "test/resources/testData"

    override fun setUp() {
        super.setUp()
    }

    override fun tearDown() {
        try {
            super.tearDown()
        } catch (e: Exception) {
            // Ignore teardown exceptions
        }
    }

    fun testSampleFilesForErrors() {
        val samplesDir = File("/Users/me/dev/apps/wasp/samples")
        if (!samplesDir.exists()) {
            println("Samples directory not found at ${samplesDir.absolutePath}")
            return
        }

        val sampleFiles = samplesDir.listFiles { file -> file.extension == "wasp" }
        if (sampleFiles == null || sampleFiles.isEmpty()) {
            println("No .wasp files found in samples directory")
            return
        }

        var totalErrors = 0
        val errorsByFile = mutableMapOf<String, List<String>>()

        for (file in sampleFiles.sortedBy { it.name }) {
            val code = file.readText()
            val psiFile = createPsiFile(file.name, code)

            // Find all error elements in the PSI tree
            val errors = PsiTreeUtil.findChildrenOfType(psiFile, PsiErrorElement::class.java)

            if (errors.isNotEmpty()) {
                val errorMessages = errors.map { error ->
                    val line = getLineNumber(psiFile.text, error.textOffset)
                    val context = getContext(psiFile.text, error.textOffset, 20)
                    "Line $line: ${error.errorDescription} near '$context'"
                }
                errorsByFile[file.name] = errorMessages
                totalErrors += errors.size

                println("❌ ${file.name}: ${errors.size} error(s)")
                errorMessages.forEach { println("   $it") }
            } else {
                println("✅ ${file.name}: no errors")
            }
        }

        println("\n=== SUMMARY ===")
        println("Total files: ${sampleFiles.size}")
        println("Files with errors: ${errorsByFile.size}")
        println("Total error count: $totalErrors")

        if (errorsByFile.isNotEmpty()) {
            println("\n=== ERRORS BY FILE ===")
            errorsByFile.forEach { (fileName, errors) ->
                println("\n$fileName (${errors.size} errors):")
                errors.forEach { println("  - $it") }
            }
        }
    }

    private fun getLineNumber(text: String, offset: Int): Int {
        return text.substring(0, minOf(offset, text.length)).count { it == '\n' } + 1
    }

    private fun getContext(text: String, offset: Int, contextSize: Int): String {
        val start = maxOf(0, offset - contextSize)
        val end = minOf(text.length, offset + contextSize)
        return text.substring(start, end).replace("\n", "\\n")
    }
}
