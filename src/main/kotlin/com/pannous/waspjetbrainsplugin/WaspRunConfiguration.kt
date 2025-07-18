package com.pannous.waspjetbrainsplugin

import com.intellij.execution.Executor
import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.InvalidDataException
import com.intellij.openapi.util.JDOMExternalizable
import com.intellij.openapi.util.WriteExternalException
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import org.jdom.Element

class WaspRunConfiguration(project: Project, factory: ConfigurationFactory, name: String) :
    RunConfigurationBase<WaspRunConfigurationOptions>(project, factory, name) {

    private var scriptPath: String = ""
    private val envVars = EnvironmentVariablesComponent()

    override fun getOptions(): WaspRunConfigurationOptions {
        return super.getOptions() as WaspRunConfigurationOptions
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return WaspRunConfigurationEditor(project)
    }

    override fun checkConfiguration() {
        if (scriptPath.isEmpty()) {
            throw RuntimeConfigurationError("Please specify a Wasp file to run")
        }
        val file = VirtualFileManager.getInstance().findFileByUrl("file://$scriptPath")
        if (file == null || !file.exists()) {
            throw RuntimeConfigurationError("Wasp file not found: $scriptPath")
        }
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return WaspRunProfileState(environment, this)
    }

    fun getScriptPath(): String = scriptPath
    fun setScriptPath(path: String) {
        scriptPath = path
    }

    fun getEnvVars(): Map<String, String> = envVars.envs
    fun setEnvVars(vars: Map<String, String>) {
        envVars.envs = vars.toMutableMap()
    }

    @Throws(WriteExternalException::class)
    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.setAttribute("scriptPath", scriptPath)
        EnvironmentVariablesComponent.writeExternal(element, envVars.envs)
    }

    @Throws(InvalidDataException::class)
    override fun readExternal(element: Element) {
        super.readExternal(element)
        scriptPath = element.getAttributeValue("scriptPath") ?: ""
        EnvironmentVariablesComponent.readExternal(element, envVars.envs)
    }
}

