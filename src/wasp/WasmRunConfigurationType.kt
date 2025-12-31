package wasp

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import javax.swing.Icon

class WasmRunConfigurationType : ConfigurationType {
    override fun getDisplayName(): String = "WASM"

    override fun getConfigurationTypeDescription(): String = "Run WebAssembly file with wasmtime"

    override fun getIcon(): Icon = WaspIcons.FILE

    override fun getId(): String = "WasmRunConfiguration"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(WasmConfigurationFactory(this))
    }
}

class WasmConfigurationFactory(type: WasmRunConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "WASM"

    override fun createTemplateConfiguration(project: com.intellij.openapi.project.Project) =
        WasmRunConfiguration(project, this, "Run WASM")
}
