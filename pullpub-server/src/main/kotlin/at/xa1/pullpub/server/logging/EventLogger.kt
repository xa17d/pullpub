package at.xa1.pullpub.server.logging

interface EventLogger {
    fun error(tag: String, message: String, exception: Exception? = null)
    fun info(tag: String, message: String)
}
