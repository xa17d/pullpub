package at.xa1.pullpub.server

import at.xa1.pullpub.server.admin.adminModule
import at.xa1.pullpub.server.logging.Logging
import at.xa1.pullpub.server.repository.Repository
import io.ktor.application.Application

fun Application.addPullpubModules(
    adminPath: String,
    logging: Logging,
    repository: Repository
) {
    includeJson()
    adminModule(adminPath, logging, repository)
}
