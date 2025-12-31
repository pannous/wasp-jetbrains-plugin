package wasp

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class WasmRunAction : AnAction("Run WASM File") {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        if (file.extension != "wasm") return

        val runManager = RunManager.getInstance(project)
        val configurationType = WasmRunConfigurationType()
        val factory = configurationType.configurationFactories[0]
        val settings = runManager.createConfiguration("Run ${file.name}", factory)
        val configuration = settings.configuration as WasmRunConfiguration

        configuration.scriptPath = file.path
        runManager.addConfiguration(settings)
        runManager.selectedConfiguration = settings

        ProgramRunnerUtil.executeConfiguration(settings, DefaultRunExecutor.getRunExecutorInstance())
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = file?.extension == "wasm"
    }
}
