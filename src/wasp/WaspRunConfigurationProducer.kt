package wasp

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

class WaspRunConfigurationProducer : LazyRunConfigurationProducer<WaspRunConfiguration>() {
    
    override fun getConfigurationFactory(): ConfigurationFactory {
        return WaspRunConfigurationType().configurationFactories[0]
    }
    
    override fun isConfigurationFromContext(
        configuration: WaspRunConfiguration, 
        context: ConfigurationContext
    ): Boolean {
        val element = context.psiLocation ?: return false
        val file = element.containingFile as? WaspFile ?: return false
        return configuration.scriptPath == file.virtualFile.path
    }
    
    override fun setupConfigurationFromContext(
        configuration: WaspRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val element = context.psiLocation ?: return false
        val file = element.containingFile as? WaspFile ?: return false
        
        configuration.scriptPath = file.virtualFile.path
        configuration.name = "Run ${file.name}"
        return true
    }
}