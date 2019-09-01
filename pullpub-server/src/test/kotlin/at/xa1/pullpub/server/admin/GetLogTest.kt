package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.fromJson
import at.xa1.pullpub.server.testServer
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GetLogTest {
    @Test
    fun `Response contains entries from log`() = testServer {
        logging.logger.apply {
            info("InfoTag", "InfoMessage")
            error("ErrorTag", "ErrorMessage", IllegalStateException("ExceptionMessage"))
        }

        withRequest(HttpMethod.Get, "/admin/log") {
            assertEquals(HttpStatusCode.OK, response.status())

            val jsonResponse = response.fromJson<LogResponse>()
            assertEquals(
                LogResponse(
                    listOf(
                        LogEntryResponse("ErrorTag", "ErrorMessage", "ExceptionMessage"),
                        LogEntryResponse("InfoTag", "InfoMessage", null)
                    )
                ),
                jsonResponse
            )
        }
    }
}
