package at.xa1.pullpub.server

import at.xa1.pullpub.server.admin.adminModule
import at.xa1.pullpub.server.application.installCallExceptionLogging
import at.xa1.pullpub.server.files.FileProvider
import at.xa1.pullpub.server.logging.Logging
import at.xa1.pullpub.server.repository.Repository
import io.ktor.application.Application

fun Application.addPullpubModules(
    adminPath: String,
    logging: Logging,
    repository: Repository,
    fileProvider: FileProvider
) {
    includeJson()
    adminModule(adminPath, logging, repository)
    staticFiles(fileProvider)
    installCallExceptionLogging(logging.logger)
}
