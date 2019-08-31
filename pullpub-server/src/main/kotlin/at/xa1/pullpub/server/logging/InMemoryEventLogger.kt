package at.xa1.pullpub.server.logging

import java.util.LinkedList

class InMemoryEventLogger(
    private val maxCapacity: Int = 500
) : EventLogger, Iterable<LogEntry> {
    private val entries = LinkedList<LogEntry>()

    private fun addEntry(entry: LogEntry) {
        while (entries.size >= maxCapacity) {
            entries.removeLast()
        }

        entries.addFirst(entry)
    }

    override fun error(tag: String, message: String, exception: Exception?) =
        addEntry(LogEntry.Error(tag, message, exception))

    override fun info(tag: String, message: String) =
        addEntry(LogEntry.Info(tag, message))

    override fun iterator(): Iterator<LogEntry> = entries.iterator()
}

sealed class LogEntry {
    data class Error(val tag: String, val message: String, val exception: Exception?) : LogEntry()
    data class Info(val tag: String, val message: String) : LogEntry()
}
