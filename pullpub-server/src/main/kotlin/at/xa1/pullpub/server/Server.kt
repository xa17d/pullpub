package at.xa1.pullpub.server

import at.xa1.pullpub.server.admin.adminModule
import at.xa1.pullpub.server.repository.Repository
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class Server(
    private val port: Int,
    private val repository: Repository
) {

    fun startBlocking() {
        val server = embeddedServer(Netty, port = port) {
            adminModule("/admin", repository)
        }
        server.start(wait = true)
    }
}
