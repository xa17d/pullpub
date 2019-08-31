package at.xa1.pullpub.server

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson

fun Application.includeJson() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
}
