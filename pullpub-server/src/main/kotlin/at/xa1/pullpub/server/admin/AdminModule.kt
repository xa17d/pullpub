package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.repository.Repository
import at.xa1.pullpub.server.respondData
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

fun Application.adminModule(
    path: String,
    repository: Repository
) {
    routing {
        route(path) {
            get {
                call.respondText("Admin!", ContentType.Text.Plain)
            }

            post("pull") {
                call.respondData(
                    pullCommand(repository)
                )
            }
        }
    }
}
