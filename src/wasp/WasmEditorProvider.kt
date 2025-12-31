package wasp

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class WasmEditorProvider : FileEditorProvider, DumbAware {
    private val LOG = Logger.getInstance(WasmEditorProvider::class.java)

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file.extension?.lowercase() == "wasm"
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return WasmFileEditor(project, file)
    }

    override fun getEditorTypeId(): String = "wasm-decompiler-editor"

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}
