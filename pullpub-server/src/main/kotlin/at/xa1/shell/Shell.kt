package at.xa1.shell

interface Shell {
    suspend fun run(vararg command: String): ShellResult
}

data class ShellResult(
    val exitCode: Int,
    val output: String
)
