package at.xa1.pullpub.server

import at.xa1.pullpub.server.admin.adminModule
import at.xa1.pullpub.server.logging.InMemoryEventLogger
import at.xa1.pullpub.server.repository.MockRepository
import io.ktor.http.HttpMethod
import io.ktor.server.testing.*

fun testServer(
    adminPath: String = "/admin",
    log: InMemoryEventLogger = InMemoryEventLogger(),
    repository: MockRepository = MockRepository(),
    testBlock: TestServerContext.() -> Unit
) = withTestApplication({
    addPullpubModules(adminPath, log, repository)
}) {
    testBlock(TestServerContext(this, log, repository))
}

class TestServerContext(
    private val engine: TestApplicationEngine,
    val log: InMemoryEventLogger,
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
