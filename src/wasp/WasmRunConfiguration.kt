package wasp

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.InvalidDataException
import com.intellij.openapi.util.WriteExternalException
import org.jdom.Element
import java.io.File

class WasmRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<RunConfigurationOptions>(project, factory, name) {

    var scriptPath: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return WasmRunConfigurationEditor()
    }

    override fun checkConfiguration() {
        if (scriptPath.isBlank()) {
            throw RuntimeConfigurationException("Please specify a WASM file to run")
        }
        val file = File(scriptPath)
        if (!file.exists()) {
            throw RuntimeConfigurationException("WASM file does not exist: $scriptPath")
        }
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return object : CommandLineState(environment) {
            override fun startProcess(): OSProcessHandler {
                val commandLine = GeneralCommandLine()
                commandLine.exePath = findWasmtime()
                commandLine.addParameter(scriptPath)
                commandLine.workDirectory = environment.project.basePath?.let { File(it) }

                return ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
            }
        }
    }

    private fun findWasmtime(): String {
        val locations = listOf(
            System.getProperty("user.home") + "/.wasmtime/bin/wasmtime",
            "/opt/homebrew/bin/wasmtime",
            "/usr/local/bin/wasmtime",
            "/usr/bin/wasmtime"
        )

        for (location in locations) {
            val file = File(location)
            if (file.exists() && file.canExecute()) {
                return location
            }
        }

        return "wasmtime"
    }

    @Throws(InvalidDataException::class)
    override fun readExternal(element: Element) {
        super.readExternal(element)
        scriptPath = element.getAttributeValue("scriptPath") ?: ""
    }

    @Throws(WriteExternalException::class)
    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.setAttribute("scriptPath", scriptPath)
    }
}
