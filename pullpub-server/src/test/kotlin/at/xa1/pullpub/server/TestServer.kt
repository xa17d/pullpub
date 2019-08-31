package at.xa1.pullpub.server

import at.xa1.pullpub.server.files.FileProvider
import at.xa1.pullpub.server.files.MockFileProvider
import at.xa1.pullpub.server.logging.InMemoryEventLogger
import at.xa1.pullpub.server.logging.Logging
import at.xa1.pullpub.server.logging.createInMemoryLogging
import at.xa1.pullpub.server.repository.MockRepository
import io.ktor.http.HttpMethod
import io.ktor.server.testing.*

fun testServer(
    adminPath: String = "/admin",
    logging: Logging = createInMemoryLogging(),
    repository: MockRepository = MockRepository(),
    fileProvider: MockFileProvider = MockFileProvider(),
    testBlock: TestServerContext.() -> Unit
) = withTestApplication({
    addPullpubModules(adminPath, logging, repository, fileProvider)
}) {
    testBlock(TestServerContext(this, logging, repository, fileProvider))
}

class TestServerContext(
    private val engine: TestApplicationEngine,
    val logging: Logging,
    val repository: MockRepository,
    val fileProvider: MockFileProvider
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
