package at.xa1.pullpub.server

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import kotlin.test.assertEquals

class AdminModuleTest {
    @Test
    fun `admin page reachable`() = withTestApplication({
        adminModule("/admin")
    }) {
        with(handleRequest(HttpMethod.Get, "/admin")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("Admin!", response.content)
        }
    }
}