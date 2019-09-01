package at.xa1.pullpub.server.application

import at.xa1.pullpub.server.logging.LogEntry
import at.xa1.pullpub.server.logging.createInMemoryLogging
import at.xa1.pullpub.server.logging.last
import io.ktor.application.call
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InstallCallExceptionLoggingTest {

    private val logging = createInMemoryLogging()

    private fun withSetupTestApplication(testBlock: TestApplicationEngine.() -> Unit) =
        withTestApplication({
            routing {
                get("/success") { call.respondText("success") }
                get("/error") { throw IllegalStateException("error") }
            }
            installCallExceptionLogging(logging.logger)
        }, testBlock)

    @Test
    fun `Nothing is logged on success`() = withSetupTestApplication {
        with(handleRequest(HttpMethod.Get, "/success")) {
            assertEquals(OK, response.status())
            assertEquals(null, logging.repository.last())
        }
    }

    @Test
    fun `Error is logged when Exception is thrown`() = withSetupTestApplication {
        try {
            handleRequest(HttpMethod.Get, "/error")
        } catch (e: IllegalStateException) {
            // e must be caught, because it is rethrown in installCallExceptionLogging
        }

        val logEntry = logging.repository.last() as LogEntry.Error

        assertTrue(logEntry.message.contains("GET /error"))
        assertTrue(logEntry.exception is IllegalStateException)
    }
}
