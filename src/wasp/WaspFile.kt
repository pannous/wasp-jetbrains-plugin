package wasp

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class WaspFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, WaspLanguage.INSTANCE) {
    
    override fun getFileType(): FileType = WaspFileType.INSTANCE
    
    override fun toString(): String = "WaspFile:${virtualFile?.name}"
}