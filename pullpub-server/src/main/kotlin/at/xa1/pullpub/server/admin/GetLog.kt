package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.DataResponse
import at.xa1.pullpub.server.logging.EventLogRepository
import at.xa1.pullpub.server.logging.LogEntry
import io.ktor.http.HttpStatusCode.Companion.OK

fun getLog(logRepository: EventLogRepository, count: Int): DataResponse<LogResponse> {
    val response = LogResponse(
        logRepository
            .getLatest(count)
            .map { entry ->
                when (entry) {
                    is LogEntry.Info -> LogEntryResponse(
                        entry.tag,
                        entry.message,
                        null
                    )
                    is LogEntry.Error -> LogEntryResponse(
                        entry.tag,
                        entry.message,
                        entry.exception?.message
                    )
                }
            }
            .toList()
    )

    return DataResponse(OK, response)
}

data class LogResponse(
    val entries: List<LogEntryResponse>
)

data class LogEntryResponse(
    val tag: String,
    val message: String,
    val errorMessage: String?
)

