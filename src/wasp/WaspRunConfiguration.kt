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

class WaspRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<WaspRunConfigurationOptions>(project, factory, name) {
    
    public override fun getOptions(): WaspRunConfigurationOptions {
        return super.getOptions() as WaspRunConfigurationOptions
    }
    
    var scriptPath: String
        get() = options.scriptPath
        set(value) { options.scriptPath = value }
    
    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return WaspRunConfigurationEditor()
    }
    
    override fun checkConfiguration() {
        if (scriptPath.isBlank()) {
            throw RuntimeConfigurationException("Please specify a Wasp file to run")
        }
    }
    
    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return object : CommandLineState(environment) {
            override fun startProcess(): OSProcessHandler {
                val commandLine = GeneralCommandLine()
                commandLine.exePath = "wasp"
                commandLine.addParameter(scriptPath)
                commandLine.workDirectory = environment.project.basePath?.let { java.io.File(it) }
                
                return ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
            }
        }
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

class WaspRunConfigurationOptions : RunConfigurationOptions() {
    var scriptPath: String = ""
}