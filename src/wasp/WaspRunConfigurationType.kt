package wasp

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import javax.swing.Icon

class WaspRunConfigurationType : ConfigurationType {
    override fun getDisplayName(): String = "Wasp"
    
    override fun getConfigurationTypeDescription(): String = "Run Wasp files"
    
    override fun getIcon(): Icon = WaspIcons.FILE
    
    override fun getId(): String = "WaspRunConfiguration"
    
    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(WaspConfigurationFactory(this))
    }
}

class WaspConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "Wasp"
    
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return WaspRunConfiguration(project, this, "Wasp")
    }
}