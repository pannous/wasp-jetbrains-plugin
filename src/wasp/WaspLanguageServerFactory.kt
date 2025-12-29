package wasp

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.LanguageServerFactory
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl
import com.redhat.devtools.lsp4ij.server.StreamConnectionProvider
import org.eclipse.lsp4j.jsonrpc.messages.Message
import org.eclipse.lsp4j.services.LanguageServer
import java.io.InputStream
import java.io.OutputStream

class WaspLanguageServerFactory : LanguageServerFactory {
    override fun createConnectionProvider(project: Project): StreamConnectionProvider {
        return WaspStreamConnectionProvider(project)
    }
}

class WaspStreamConnectionProvider(private val project: Project) : StreamConnectionProvider {
    private var process: Process? = null

    override fun start() {
        val waspExecutable = WaspExecutableFinder.findWaspExecutable()
        val command = listOf(waspExecutable, "lsp")

        val processBuilder = ProcessBuilder(command)
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)

        process = processBuilder.start()
    }

    override fun getInputStream(): InputStream {
        return process?.inputStream ?: throw IllegalStateException("LSP server not started")
    }

    override fun getOutputStream(): OutputStream {
        return process?.outputStream ?: throw IllegalStateException("LSP server not started")
    }

    override fun stop() {
        process?.destroy()
        process = null
    }
}
