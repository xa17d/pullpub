package at.xa1.pullpub.server.repository

interface Repository {
    suspend fun pull(): PullResult
    suspend fun getActiveCommit(): Commit
}

enum class PullResult {
    Updated,
    AlreadyUpToDate
}

data class Commit(
    val id: String,
    val message: String,
    val author: String
)

class RepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)