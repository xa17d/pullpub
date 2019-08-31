package at.xa1.pullpub.server

import at.xa1.pullpub.server.admin.adminModule
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