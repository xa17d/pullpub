package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.DataResponse
import at.xa1.pullpub.server.logging.EventLogger
import at.xa1.pullpub.server.repository.PullResult.AlreadyUpToDate
import at.xa1.pullpub.server.repository.PullResult.Updated
import at.xa1.pullpub.server.repository.Repository
import at.xa1.pullpub.server.repository.RepositoryException
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK

suspend fun pullCommand(
    log: EventLogger,
    repository: Repository
): DataResponse<PullResponse> =
    try {
        val pullResult = repository.pull()

        DataResponse(
            OK,
            when (pullResult) {
                Updated -> PullResponse(
                    "UPDATED",
                    "Pull successful and repository was updated"
                )
                AlreadyUpToDate -> PullResponse(
                    "ALREADY_UP_TO_DATE",
                    "Pull successful, repository was already up to date"
                )
            }
        ).also {
            log.info(PULL_TAG, it.data.message)
        }
    } catch (e: RepositoryException) {
        log.error(PULL_TAG, "Error during pull", e)
        DataResponse(
            InternalServerError,
            PullResponse(
                "ERROR",
                "Error during pull. Check logs."
            )
        )
    }

private const val PULL_TAG = "Pull"

data class PullResponse(
    val status: String,
    val message: String
)
