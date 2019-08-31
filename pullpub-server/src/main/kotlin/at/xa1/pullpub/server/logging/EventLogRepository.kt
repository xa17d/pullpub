package at.xa1.pullpub.server.logging

interface EventLogRepository {
    fun getLatest(count: Int = 1000): Iterable<LogEntry>
}

fun EventLogRepository.last() = getLatest(1).firstOrNull()
