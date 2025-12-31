package wasp

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

class WasmFileType : FileType {

    companion object {
        val INSTANCE = WasmFileType()
    }

    override fun getName(): String = "WASM"

    override fun getDescription(): String = "WebAssembly Binary File"

    override fun getDefaultExtension(): String = "wasm"

    override fun getIcon(): Icon? = WaspIcons.FILE

    override fun isBinary(): Boolean = true

    override fun isReadOnly(): Boolean = false
}
