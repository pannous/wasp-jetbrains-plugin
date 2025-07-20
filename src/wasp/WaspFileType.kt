package wasp

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class WaspFileType : LanguageFileType(WaspLanguage.INSTANCE) {
    
    companion object {
        val INSTANCE = WaspFileType()
    }
    
    override fun getName(): String = "Wasp"
    
    override fun getDescription(): String = "Wasp Language File"
    
    override fun getDefaultExtension(): String = "wasp"
    
    override fun getIcon(): Icon? = WaspIcons.FILE
}