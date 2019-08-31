package at.xa1.pullpub.server.logging

import java.util.LinkedList

class InMemoryEventLogger(
    private val repository: InMemoryEventLogRepository
) : EventLogger {
    override fun error(tag: String, message: String, exception: Exception?) =
        repository.addEntry(LogEntry.Error(tag, message, exception))

    override fun info(tag: String, message: String) =
        repository.addEntry(LogEntry.Info(tag, message))
}

class InMemoryEventLogRepository(
    private val maxCapacity: Int
) : EventLogRepository {
    private val entries = LinkedList<LogEntry>()

    fun addEntry(entry: LogEntry) {
        while (entries.size >= maxCapacity) {
            entries.removeLast()
        }

        entries.addFirst(entry)
    }

    override fun getLatest(count: Int): Iterable<LogEntry> {
        return entries.take(count)
    }
}

fun createInMemoryLogging(maxCapacity: Int = 500): Logging {
    val repository = InMemoryEventLogRepository(maxCapacity)
    val logger = InMemoryEventLogger(repository)
    return Logging(logger, repository)
}
