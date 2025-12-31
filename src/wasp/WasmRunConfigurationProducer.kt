package wasp

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

class WasmRunConfigurationProducer : LazyRunConfigurationProducer<WasmRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory {
        return WasmConfigurationFactory(WasmRunConfigurationType())
    }

    override fun isConfigurationFromContext(
        configuration: WasmRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val file = context.location?.virtualFile ?: return false
        return file.extension == "wasm" && configuration.scriptPath == file.path
    }

    override fun setupConfigurationFromContext(
        configuration: WasmRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val file = context.location?.virtualFile ?: return false
        if (file.extension != "wasm") return false

        configuration.scriptPath = file.path
        configuration.name = "Run ${file.name}"
        return true
    }
}
