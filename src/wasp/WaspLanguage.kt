package wasp

import com.intellij.lang.Language

class WaspLanguage : Language("Wasp") {
    
    companion object {
        val INSTANCE = WaspLanguage()
    }
    
    override fun getDisplayName(): String = "Wasp"
    
    override fun isCaseSensitive(): Boolean = true
}