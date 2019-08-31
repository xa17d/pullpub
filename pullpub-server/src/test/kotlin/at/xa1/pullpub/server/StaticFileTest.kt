package at.xa1.pullpub.server

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import org.junit.Test
import kotlin.test.assertEquals

class StaticFileTest {
    @Test
    fun `a path is resolved using PathProvider`() = testServer {
        fileProvider.provideHtml("<html>test</html>")
        withRequest(HttpMethod.Get, "/my/path") {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("<html>test</html>", response.content)
            assertEquals("my/path", fileProvider.requestedPaths.last())
        }
    }
}
