package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.testServer
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpStatusCode.Companion.Found
import io.ktor.http.HttpStatusCode.Companion.OK
import org.junit.Test
import kotlin.test.assertEquals

class AdminModuleTest {
    @Test
    fun `admin page reachable`() = testServer {
        withRequest(Get, "/admin/") {
            assertEquals(OK, response.status())
        }
    }

    @Test
    fun `admin page gets redirected when slash is missing at the end`() = testServer {
        withRequest(Get, "/admin") {
            assertEquals(Found, response.status())
            assertEquals("/admin/", response.headers["Location"])
        }
    }
}
