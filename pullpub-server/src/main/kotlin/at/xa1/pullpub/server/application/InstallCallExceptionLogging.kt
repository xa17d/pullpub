package at.xa1.pullpub.server.application

import at.xa1.pullpub.server.logging.EventLogger
import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.request.httpMethod
import io.ktor.request.path

fun Application.installCallExceptionLogging(log: EventLogger) {
    install(CallLogging)
    intercept(ApplicationCallPipeline.Monitoring) {
        val request = call.request
        try {
            this.proceed()
        } catch (e: Exception) {
            log.error(
                LOG_TAG,
                "Unhandled exception during call: ${request.httpMethod.value} ${request.path()}",
                e
            )
            throw e
        }
    }
}

private const val LOG_TAG = "CallExceptionLog"
