package com.pannous.waspjetbrainsplugin

import com.intellij.execution.ExecutorRegistry
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class WaspRunAction : AnAction("Run Wasp File") {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        
        if (file.extension != "wasp") return
        
        runWaspFile(project, file)
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = file?.extension == "wasp"
    }

    private fun runWaspFile(project: Project, file: VirtualFile) {
        val runManager = RunManager.getInstance(project)
        val configurationType = WaspRunConfigurationType.getInstance()
        val factory = configurationType.configurationFactories[0]
        
        val configuration = runManager.createConfiguration("Run ${file.name}", factory)
        val waspConfig = configuration.configuration as WaspRunConfiguration
        waspConfig.setScriptPath(file.path)
        
        runManager.addConfiguration(configuration)
        runManager.selectedConfiguration = configuration
        
        val executor = ExecutorRegistry.getInstance().getExecutorById(DefaultRunExecutor.EXECUTOR_ID)
        if (executor != null) {
            ProgramRunnerUtil.executeConfiguration(configuration, executor)
        }
    }
}