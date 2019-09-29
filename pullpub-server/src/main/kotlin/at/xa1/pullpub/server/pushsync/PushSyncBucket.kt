package at.xa1.pullpub.server.pushsync

import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readBytes
import io.ktor.websocket.WebSocketServerSession
import kotlinx.coroutines.channels.consumeEach

class PushSyncBucket {
    private val sessions = mutableSetOf<WebSocketServerSession>()
    private val sessionsLock = Any()
    private var state: String = "{}"
    private val stateLock = Any()

    suspend fun processSession(session: WebSocketServerSession) {
        try {
            add(session)
            session.sendState(state)
            session.incoming.consumeEach { frame ->
                pushUpdate(session, frame.readText())
            }
        } finally {
            remove(session)
        }
    }

    private suspend fun pushUpdate(sender: WebSocketServerSession, newState: String) {
        val otherSessions: List<WebSocketServerSession>
        synchronized(stateLock) {
            state = newState
            otherSessions = sessions.filter { it != sender }.toList()
        }

        otherSessions.forEach {
            it.sendState(newState)
        }
    }

    private suspend fun WebSocketServerSession.sendState(state: String) {
        send(Frame.Text(state))
    }

    private fun add(session: WebSocketServerSession) {
        synchronized(sessionsLock) {
            sessions.add(session)
        }
    }

    private fun remove(session: WebSocketServerSession) {
        synchronized(sessionsLock) {
            sessions.remove(session)
        }
    }
}

fun Frame.readText(): String {
    return String(readBytes())
}