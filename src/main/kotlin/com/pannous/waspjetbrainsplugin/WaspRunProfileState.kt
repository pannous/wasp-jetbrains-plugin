package com.pannous.waspjetbrainsplugin

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFileManager
import java.io.File

class WaspRunProfileState(
    environment: ExecutionEnvironment,
    private val configuration: WaspRunConfiguration
) : CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val scriptPath = configuration.getScriptPath()
        val file = VirtualFileManager.getInstance().findFileByUrl("file://$scriptPath")
            ?: throw RuntimeException("Wasp file not found: $scriptPath")

        return WaspExecutor.executeWaspFile(configuration.project, file)
            ?: throw RuntimeException("Failed to start Wasp process")
    }

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        val processHandler = startProcess()
        val console = createConsole(executor)
        console?.attachToProcess(processHandler)

        return DefaultExecutionResult(console, processHandler)
    }

    override fun createConsole(executor: Executor): ConsoleView? {
        val console = super.createConsole(executor)
        console?.print("Running Wasp file: ${configuration.getScriptPath()}\n", ConsoleViewContentType.SYSTEM_OUTPUT)
        return console
    }
}
