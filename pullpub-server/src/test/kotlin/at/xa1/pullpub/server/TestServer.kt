package at.xa1.pullpub.server

import at.xa1.pullpub.server.admin.adminModule
import at.xa1.pullpub.server.repository.MockRepository
import io.ktor.http.HttpMethod
import io.ktor.server.testing.*

fun testServer(
    adminPath: String = "/admin",
    repository: MockRepository = MockRepository(),
    testBlock: TestServerContext.() -> Unit
) = withTestApplication({
    adminModule(adminPath, repository)
}) {
    testBlock(TestServerContext(this, repository))
}

class TestServerContext(
    private val engine: TestApplicationEngine,
    val repository: MockRepository
) {
    fun withRequest(
        method: HttpMethod,
        path: String,
        setup: TestApplicationRequest.() -> Unit = {},
        testBlock: TestApplicationCall.() -> Unit
    ) =
        testBlock(engine.handleRequest(method, path, setup))

    fun withRequestJson(
        method: HttpMethod,
        path: String,
        testBlock: TestApplicationCall.() -> Unit
    ) = withRequest(
        method,
        path,
        setup = {
            addHeader("Content-Type", "application/json")
        },
        testBlock = testBlock
    )
}
