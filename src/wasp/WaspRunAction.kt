package wasp

import com.intellij.execution.ExecutorRegistry
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project

class WaspRunAction : AnAction("Run Wasp File") {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        
        if (file.extension != "wasp") return
        
        val runManager = RunManager.getInstance(project)
        val factory = WaspRunConfigurationType().configurationFactories[0]
        val settings = runManager.createConfiguration("Run ${file.name}", factory)
        val configuration = settings.configuration as WaspRunConfiguration
        
        configuration.scriptPath = file.path
        runManager.addConfiguration(settings)
        runManager.selectedConfiguration = settings
        
        val executor = ExecutorRegistry.getInstance().getExecutorById(DefaultRunExecutor.EXECUTOR_ID)
        if (executor != null) {
            ProgramRunnerUtil.executeConfiguration(settings, executor)
        }
    }
    
    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = file?.extension == "wasp"
    }
}