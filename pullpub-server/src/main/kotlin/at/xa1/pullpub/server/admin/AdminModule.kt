package at.xa1.pullpub.server.admin

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.adminModule(path: String) {
    routing {
        get(path) {
            call.respondText("Admin!", ContentType.Text.Plain)
        }
    }
}