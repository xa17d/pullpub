package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.TestServerContext
import at.xa1.pullpub.server.fromJson
import at.xa1.pullpub.server.logging.LogEntry
import at.xa1.pullpub.server.logging.last
import at.xa1.pullpub.server.repository.PullResult
import at.xa1.pullpub.server.testServer
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.testing.TestApplicationCall
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PullTest {
    @Test
    fun `get is not handled for pull`() = testServer {
        withRequestJson(Get, "/admin/pull") {
            assertFalse(requestHandled)
        }
    }

    private fun TestServerContext.whenPullRequest(
        testBlock: TestApplicationCall.(contentJson: PullResponse) -> Unit
    ) {
        withRequestJson(Post, "/admin/pull") {
            testBlock(this, response.fromJson())
        }
    }

    @Test
    fun `if pull updated repository, response has status UPDATED`() = testServer {
        repository.pullResult = PullResult.Updated

        whenPullRequest { contentJson ->
            assertEquals(OK, response.status())
            assertEquals("UPDATED", contentJson.status)
            assertTrue(logging.repository.last() is LogEntry.Info)
        }
    }

    @Test
    fun `if on pull repository is alreadyUpToDate, response has status ALREADY_UP_TO_DATE`() = testServer {
        repository.pullResult = PullResult.AlreadyUpToDate

        whenPullRequest { contentJson ->
            assertEquals(OK, response.status())
            assertEquals("ALREADY_UP_TO_DATE", contentJson.status)
            assertTrue(logging.repository.last() is LogEntry.Info)
        }
    }

    @Test
    fun `if pull repository fails, response has status ERROR`() = testServer {
        repository.pullThrows = true

        whenPullRequest { contentJson ->
            assertEquals(InternalServerError, response.status())
            assertEquals("ERROR", contentJson.status)
            assertTrue(logging.repository.last() is LogEntry.Error)
        }
    }
}
