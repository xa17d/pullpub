package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.testServer
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpStatusCode.Companion.OK
import org.junit.Test
import kotlin.test.assertEquals

class AdminModuleTest {
    @Test
    fun `admin page reachable`() = testServer {
        withRequest(Get, "/admin") {
            assertEquals(OK, response.status())
        }
    }
}
