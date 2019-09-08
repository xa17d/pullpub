package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.DataResponse
import at.xa1.pullpub.server.logging.EventLogger
import at.xa1.pullpub.server.repository.Commit
import at.xa1.pullpub.server.repository.Repository
import at.xa1.pullpub.server.repository.RepositoryException
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK

suspend fun getActiveCommit(repository: Repository, logger: EventLogger): DataResponse<ActiveCommitResponse> =
    try {
        val commit = repository.getActiveCommit()
        val branchName = repository.getBranchName()
        DataResponse(OK, ActiveCommitResponse(null, commit, branchName))
    } catch (e: RepositoryException) {
        logger.error(LOG_TAG, "Cannot retrieve active commit", e)

        DataResponse(
            InternalServerError,
            ActiveCommitResponse("Error while retrieving active commit. Check logs.")
        )
    }

data class ActiveCommitResponse(
    val message: String?,
    val activeCommit: Commit? = null,
    val activeBranch: String? = null
)

private const val LOG_TAG = "GetActiveCommit"
