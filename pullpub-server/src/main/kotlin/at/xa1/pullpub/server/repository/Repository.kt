package at.xa1.pullpub.server.repository

interface Repository {
    suspend fun pull(): PullResult
    suspend fun getActiveCommit(): Commit
    suspend fun checkout(branchName: String)
    suspend fun getBranchName(): String
}

enum class PullResult {
    Updated,
    AlreadyUpToDate
}

data class Commit(
    val id: String,
    val timestamp: String,
    val message: String,
    val author: String
)

open class RepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)
