package at.xa1.pullpub.server.repository

import at.xa1.pullpub.server.repository.PullResult.AlreadyUpToDate
import at.xa1.pullpub.server.repository.PullResult.Updated
import at.xa1.pullpub.server.repository.PullResult.ForcePulled
import at.xa1.shell.Shell
import at.xa1.shell.ShellResult

class GitRepository(
    private val shell: Shell
) : Repository {
    private suspend fun runWithResult(vararg command: String): ShellResult {
        val result = shell.run(*command)
        if (result.exitCode != 0) {
            throw RepositoryShellException(
                "Shell Error: " + result.output.trim(),
                result.output
            )
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
        try {
            val result = runWithResult("git", "pull")
            return if (result.output.contains("Already up to date", ignoreCase = true)) {
                AlreadyUpToDate
            } else {
                Updated
            }
        } catch (e: RepositoryShellException) {
            /**
             * A pull after a force update looks like this:
             * ```
             * From https://github.com/xa17d/pullpub-website-example
             * + c577dd4...c10e00f master     -> origin/master  (forced update)
             * ```
             * -> requires a force pull
             */
            if (e.output.contains("forced update", ignoreCase = true)) {
                forcePull(getBranchName())
                return ForcePulled
            } else {
                throw e
            }
        }
    }

    private suspend fun forcePull(branchName: String) {
        runWithoutResult("git", "fetch", "origin/$branchName")
        runWithoutResult("git", "reset", "--hard", "origin/$branchName")
    }
}

class RepositoryShellException(
    message: String,
    val output: String,
    cause: Throwable? = null
) : RepositoryException(message, cause)