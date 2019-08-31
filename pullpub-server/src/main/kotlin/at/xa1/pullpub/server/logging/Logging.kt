package at.xa1.pullpub.server.logging

data class Logging(
    val logger: EventLogger,
    val repository: EventLogRepository
)
