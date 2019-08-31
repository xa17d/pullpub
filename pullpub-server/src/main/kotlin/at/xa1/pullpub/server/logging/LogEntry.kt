package at.xa1.pullpub.server.logging

interface LogEntryBase {
    val tag: String
    val message: String
}

sealed class LogEntry: LogEntryBase {
    data class Error(override val tag: String, override val message: String, val exception: Exception?) : LogEntry()
    data class Info(override val tag: String, override val message: String) : LogEntry()
}
