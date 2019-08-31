package at.xa1.pullpub.server

import at.xa1.pullpub.server.logging.InMemoryEventLogger
import at.xa1.pullpub.server.repository.MockRepository

fun main(args: Array<String>) {
    Server(8080, InMemoryEventLogger(), MockRepository()).startBlocking()
}
