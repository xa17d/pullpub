package at.xa1.shell

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class ProcessShell(
    private val workingDirectory: File
) {
    suspend fun run(vararg command: String): ShellResult {
        val builder = ProcessBuilder(*command).apply {
            redirectErrorStream(true)
            directory(workingDirectory)
        }

        val process = builder.start()

        try {
            while (process.isAlive) {
                delay(250)
            }
        } catch (e: CancellationException) {
            process.apply {
                destroy()
                waitFor(5, TimeUnit.SECONDS)
                destroyForcibly()
            }
            throw e
        }

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = reader.readText()

        return ShellResult(
            process.exitValue(),
            output
        )
    }
}
