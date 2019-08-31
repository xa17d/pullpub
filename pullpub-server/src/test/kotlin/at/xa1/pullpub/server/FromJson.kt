package at.xa1.pullpub.server

import com.google.gson.Gson
import io.ktor.server.testing.TestApplicationResponse

inline fun <reified T> TestApplicationResponse.fromJson(): T {
    return Gson().fromJson(content, T::class.java) as T
}
