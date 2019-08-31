package at.xa1.pullpub.server

import at.xa1.pullpub.server.files.FileProvider
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.content.LocalFileContent
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import java.io.File

fun Application.staticFiles(fileProvider: FileProvider) {
    routing {
        get("{$pathParameterName...}") {
            val relativePath = call.parameters.getAll(pathParameterName)?.joinToString(File.separator) ?: return@get
            fileProvider.getForPath(relativePath)?.let { file ->
                call.respond(LocalFileContent(file))
            }
        }
    }
}

private val pathParameterName = "static-content-path-parameter"
