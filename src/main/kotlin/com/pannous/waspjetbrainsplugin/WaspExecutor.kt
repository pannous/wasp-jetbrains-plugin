package com.pannous.waspjetbrainsplugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class WaspExecutor {
    
    companion object {
        fun executeWaspFile(project: Project, file: VirtualFile, consoleView: ConsoleView? = null): ProcessHandler? {
            val waspBinary = findWaspBinary()
            if (waspBinary == null) {
                consoleView?.print("Wasp binary not found in PATH. Please install wasp first.\n", 
                    com.intellij.execution.ui.ConsoleViewContentType.ERROR_OUTPUT)
                return null
            }
            
            val commandLine = GeneralCommandLine()
                .withExePath(waspBinary)
                .withParameters(file.path)
                .withWorkDirectory(project.basePath)
            
            return try {
                val processHandler = ProcessHandlerFactory.getInstance().createProcessHandler(commandLine)
                consoleView?.attachToProcess(processHandler)
                processHandler.startNotify()
                processHandler
            } catch (e: Exception) {
                consoleView?.print("Failed to start wasp process: ${e.message}\n", 
                    com.intellij.execution.ui.ConsoleViewContentType.ERROR_OUTPUT)
                null
            }
        }
        
        private fun findWaspBinary(): String? {
            val pathEnv = System.getenv("PATH") ?: return null
            val paths = pathEnv.split(File.pathSeparator)
            
            for (path in paths) {
                val waspFile = File(path, "wasp")
                if (waspFile.exists() && waspFile.canExecute()) {
                    return waspFile.absolutePath
                }
            }
            
            return null
        }
    }
}