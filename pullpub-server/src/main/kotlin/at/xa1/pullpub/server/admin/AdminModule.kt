package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.logging.Logging
import at.xa1.pullpub.server.repository.Repository
import at.xa1.pullpub.server.repository.RepositoryException
import at.xa1.pullpub.server.respondData
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.html.respondHtml
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
                val activeCommit =
                    try {
                        repository.getActiveCommit()
                    } catch (e: RepositoryException) {
                        logging.logger.error(LOG_TAG, "Cannot retrieve active commit", e)
                        null
                    }
                call.respondHtml {
                    adminStatusPage(logging.repository, activeCommit)
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
        }
    }
}

private const val LOG_TAG = "AdminModule"
