package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.fromJson
import at.xa1.pullpub.server.repository.Commit
import at.xa1.pullpub.server.testServer
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GetActiveCommitTest {
    @Test
    fun `active commit is returned`() = testServer {
        val testCommit = Commit(
            id = "TestId",
            timestamp = "TestTimestamp",
            message = "TestMessage",
            author = "TestAuthor"
        )
        repository.activeCommit = testCommit
        withRequest(HttpMethod.Get, "/admin/activecommit") {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                ActiveCommitResponse(
                    null,
                    testCommit
                )
                , response.fromJson()
            )
        }
    }

    @Test
    fun `error is propagated`() = testServer {
        repository.activeCommitThrows = true
        withRequest(HttpMethod.Get, "/admin/activecommit") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
            val jsonResponse = response.fromJson<ActiveCommitResponse>()
            assertEquals(null, jsonResponse.activeCommit)
            assertNotNull(jsonResponse.message)
        }
    }
}
