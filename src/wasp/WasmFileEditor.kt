package wasp

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel
import java.awt.BorderLayout
import java.io.File

class WasmFileEditor(
    private val project: Project,
    private val file: VirtualFile
) : FileEditor {
    private val LOG = Logger.getInstance(WasmFileEditor::class.java)
    private val component: JPanel
    private val editor: EditorEx

    init {
        val watContent = decompileWasm(file)

        val editorFactory = EditorFactory.getInstance()
        val document = editorFactory.createDocument(watContent)
        editor = editorFactory.createEditor(document, project) as EditorEx
        editor.settings.isLineNumbersShown = true
        editor.setViewer(true)

        component = JPanel(BorderLayout())
        component.add(editor.component, BorderLayout.CENTER)
    }

    private fun decompileWasm(wasmFile: VirtualFile): String {
        return try {
            val wasm2wat = findWasm2Wat()
            if (wasm2wat == null) {
                "Error: wasm2wat not found. Please install wabt toolkit.\n" +
                "  brew install wabt"
            } else {
                val process = ProcessBuilder(wasm2wat, wasmFile.path)
                    .redirectErrorStream(true)
                    .start()

                val output = process.inputStream.bufferedReader().readText()
                val exitCode = process.waitFor()

                if (exitCode == 0) {
                    output
                } else {
                    "Error decompiling WASM file:\n$output"
                }
            }
        } catch (e: Exception) {
            LOG.warn("Failed to decompile WASM file: ${wasmFile.path}", e)
            "Error: ${e.message}"
        }
    }

    private fun findWasm2Wat(): String? {
        val locations = listOf(
            "/opt/homebrew/bin/wasm2wat",
            "/usr/local/bin/wasm2wat",
            "/usr/bin/wasm2wat"
        )

        for (location in locations) {
            val file = File(location)
            if (file.exists() && file.canExecute()) {
                return location
            }
        }

        return "wasm2wat"
    }

    override fun getComponent(): JComponent = component

    override fun getPreferredFocusedComponent(): JComponent? = editor.contentComponent

    override fun getName(): String = "WASM Decompiled"

    override fun setState(state: FileEditorState) {}

    override fun isModified(): Boolean = false

    override fun isValid(): Boolean = true

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}

    override fun dispose() {
        EditorFactory.getInstance().releaseEditor(editor)
    }

    override fun <T : Any?> getUserData(key: Key<T>): T? = null

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {}

    override fun getFile(): VirtualFile = file
}
