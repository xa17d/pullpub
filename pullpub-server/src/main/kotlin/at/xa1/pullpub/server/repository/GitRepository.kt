package at.xa1.pullpub.server.repository

import at.xa1.pullpub.server.repository.PullResult.AlreadyUpToDate
import at.xa1.pullpub.server.repository.PullResult.Updated
import at.xa1.shell.Shell
import at.xa1.shell.ShellResult

class GitRepository(
    private val shell: Shell
) : Repository {
    private suspend fun runWithResult(vararg command: String): ShellResult {
        val result = shell.run(*command)
        if (result.exitCode != 0) {
            throw RepositoryException("Shell Error: " + result.output.trim())
        }
        return result
    }

    private suspend fun runWithoutResult(vararg command: String) {
        runWithResult(*command)
    }

    suspend fun clone(repoUrl: String): Unit = runWithoutResult("git", "clone", repoUrl, ".")

    override suspend fun checkout(branchName: String) {
        runWithResult("git", "checkout", branchName)
    }

    override suspend fun getBranchName(): String {
        val result = runWithResult("git", "symbolic-ref", "--short", "HEAD")
        return result.output.trim()
    }

    suspend fun isInitialized(): Boolean {
        return shell.run("git", "status").exitCode == 0
    }

    override suspend fun getActiveCommit(): Commit =
        runWithResult("git", "log", "--pretty=format:%h§§§%ad§§§%an§§§%ae§§§%s", "--max-count=1").let {
            val parts = it.output.split("§§§", limit = 5)
            Commit(
                id = parts[0],
                timestamp = parts[1],
                author = "${parts[2]} (${parts[3]})",
                message = parts[4]
            )
        }

    override suspend fun pull(): PullResult {
        val result = runWithResult("git", "pull")
        return if (result.output.contains("Already up to date", ignoreCase = true)) {
            AlreadyUpToDate
        } else {
            Updated
        }
    }
}
