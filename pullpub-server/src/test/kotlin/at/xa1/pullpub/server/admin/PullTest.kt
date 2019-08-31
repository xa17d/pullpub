package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.repository.PullResult
import at.xa1.pullpub.server.testServer
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PullTest {
    @Test
    fun `get is not handled for pull`() = testServer {
        withRequestJson(Get, "/admin/pull") {
            assertFalse(requestHandled)
        }
    }

    @Test
    fun `if pull updated repository, response has status UPDATED`() = testServer {
        repository.pullResult = PullResult.Updated

        withRequestJson(Post, "/admin/pull") {
            assertEquals(OK, response.status())
            assertEquals("UPDATED", response.content)
        }
    }

    @Test
    fun `if on pull repository is alreadyUpToDate, response has status ALREADY_UP_TO_DATE`() = testServer {
        repository.pullResult = PullResult.AlreadyUpToDate

        withRequestJson(Post, "/admin/pull") {
            assertEquals(OK, response.status())
            assertEquals("ALREADY_UP_TO_DATE", response.content)
        }
    }

    @Test
    fun `if pull repository fails, response has status ERROR`() = testServer {
        repository.pullThrows = true

        withRequestJson(Post, "/admin/pull") {
            assertEquals(InternalServerError, response.status())
            assertEquals("ERROR", response.content)
        }
    }
}
