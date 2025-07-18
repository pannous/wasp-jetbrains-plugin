package com.pannous.waspjetbrainsplugin

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import javax.swing.Icon

class WaspRunConfigurationType : ConfigurationType {

    override fun getDisplayName(): String = "Wasp"

    override fun getConfigurationTypeDescription(): String = "Run Wasp files"

    override fun getIcon(): Icon? = null

    override fun getId(): String = "WASP_RUN_CONFIGURATION"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(WaspConfigurationFactory(this))

    companion object {
        fun getInstance(): WaspRunConfigurationType = ConfigurationType.CONFIGURATION_TYPE_EP.findExtension(WaspRunConfigurationType::class.java)!!
    }
}

class WaspConfigurationFactory(type: WaspRunConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = "Wasp"

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return WaspRunConfiguration(project, this, "Wasp")
    }

    override fun getOptionsClass(): Class<out BaseState> = WaspRunConfigurationOptions::class.java
}