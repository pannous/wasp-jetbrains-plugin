package wasp

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import java.io.File

object WaspExecutableFinder {
    private val LOG = Logger.getInstance(WaspExecutableFinder::class.java)

    fun findWaspExecutable(): String {
        // 1. Try bundled executable first
        val bundled = findBundledExecutable()
        if (bundled != null) {
            LOG.info("Using bundled wasp executable: $bundled")
            return bundled
        }

        // 2. Try common installation locations
        val commonLocations = listOf(
            "/opt/homebrew/bin/wasp",  // Homebrew on Apple Silicon
            "/usr/local/bin/wasp",     // Homebrew on Intel Mac / standard Unix
            "/usr/bin/wasp"            // System-wide installation
        )

        for (location in commonLocations) {
            val file = File(location)
            if (file.exists() && file.canExecute()) {
                LOG.info("Using wasp executable from: $location")
                return location
            }
        }

        // 3. Fallback to PATH
        LOG.info("Using wasp from PATH")
        return "wasp"
    }

    private fun findBundledExecutable(): String? {
        try {
            val classLoader = WaspExecutableFinder::class.java.classLoader
            val arch = System.getProperty("os.arch").lowercase()
            val platformPath = when {
                SystemInfo.isMac && SystemInfo.isAarch64 -> "bin/mac/arm/wasp"
                SystemInfo.isMac && arch.contains("x86") -> "bin/mac/x86/wasp"
                SystemInfo.isMac -> "bin/mac/wasp"
                SystemInfo.isLinux && SystemInfo.isAarch64 -> "bin/linux-arm64/wasp"
                else -> return null
            }

            val resource = classLoader.getResourceAsStream(platformPath) ?: return null

            // Extract to temp file (needed when running from JAR)
            val tempDir = File(System.getProperty("java.io.tmpdir"), "wasp-plugin")
            tempDir.mkdirs()
            val tempFile = File(tempDir, "wasp")

            tempFile.outputStream().use { output ->
                resource.copyTo(output)
            }

            // Make executable
            tempFile.setExecutable(true)
            LOG.info("Extracted bundled wasp executable to: ${tempFile.absolutePath}")
            return tempFile.absolutePath

        } catch (e: Exception) {
            LOG.warn("Failed to find bundled wasp executable", e)
        }

        return null
    }
}
