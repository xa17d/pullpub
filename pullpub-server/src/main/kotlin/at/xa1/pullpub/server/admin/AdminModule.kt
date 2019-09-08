package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.logging.Logging
import at.xa1.pullpub.server.repository.Repository
import at.xa1.pullpub.server.respondData
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.path
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

fun Application.adminModule(
    path: String,
    logging: Logging,
    repository: Repository
) {
    routing {
        route(path) {
            get {
                if (!call.request.path().endsWith("/")) {
                    call.respondRedirect(call.request.path() + "/")
                } else {
                    call.respondText(
                        ContentType.parse("text/html"),
                        HttpStatusCode.OK
                    ) {
                        adminStatusPage()
                    }
                }
            }

            post("pull") {
                call.respondData(
                    pullCommand(logging.logger, repository)
                )
            }

            get("activecommit") {
                call.respondData(getActiveCommit(repository, logging.logger))
            }

            get("log") {
                call.respondData(getLog(logging.repository, 500))
            }
        }
    }
}

private const val LOG_TAG = "AdminModule"
