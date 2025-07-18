package com.pannous.waspjetbrainsplugin

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.pannous.wasp.WaspFile

class WaspRunConfigurationProducer : LazyRunConfigurationProducer<WaspRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory {
        return WaspRunConfigurationType.getInstance().configurationFactories[0]
    }

    override fun isConfigurationFromContext(configuration: WaspRunConfiguration, context: ConfigurationContext): Boolean {
        val location = context.location ?: return false
        val file = location.virtualFile ?: return false
        
        return file.extension == "wasp" && configuration.getScriptPath() == file.path
    }

    override fun setupConfigurationFromContext(
        configuration: WaspRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val location = context.location ?: return false
        val file = location.virtualFile ?: return false
        
        if (file.extension != "wasp") return false
        
        val psiFile = location.psiElement?.containingFile
        if (psiFile !is WaspFile) return false
        
        configuration.setScriptPath(file.path)
        configuration.name = "Run ${file.name}"
        
        return true
    }
}