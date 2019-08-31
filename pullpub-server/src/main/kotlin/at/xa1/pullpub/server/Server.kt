package at.xa1.pullpub.server

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class Server(private val port: Int) {

    fun startBlocking() {
        val server = embeddedServer(Netty, port = port) {
            adminModule("/admin")
        }
        server.start(wait = true)
    }
}