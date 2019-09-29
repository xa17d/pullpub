package at.xa1.pullpub.server.pushsync

import at.xa1.pullpub.server.logging.Logging
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.time.Duration

fun Application.pushSyncModule(
    path: String,
    logging: Logging
) {
    install(WebSockets) {
        pingPeriod = Duration.ofMinutes(1)
    }

    val pushSyncBucket = PushSyncBucket()
    routing {
        webSocket(path) {
            pushSyncBucket.processSession(this)
        }
    }
}
