package at.xa1.pullpub.server

import at.xa1.pullpub.server.admin.adminModule
import at.xa1.pullpub.server.files.FileProvider
import at.xa1.pullpub.server.logging.EventLogger
import at.xa1.pullpub.server.logging.Logging
import at.xa1.pullpub.server.repository.Repository
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class Server(
    private val port: Int,
    private val adminPath: String,
    private val logging: Logging,
    private val repository: Repository,
    private val fileProvider: FileProvider
) {

    fun startBlocking() {
        val server = embeddedServer(Netty, port = port) {
            addPullpubModules(adminPath, logging, repository, fileProvider)
        }
        server.start(wait = true)
    }
}
