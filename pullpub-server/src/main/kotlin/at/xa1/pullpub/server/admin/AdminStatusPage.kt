package at.xa1.pullpub.server.admin

import java.io.IOException
import java.io.InputStreamReader

fun adminStatusPage(): String {
    return getResourceFileAsString("AdminStatusPage.html")
}

@Throws(IOException::class)
fun getResourceFileAsString(fileName: String): String {
    val classLoader = ClassLoader.getSystemClassLoader()
    val stream = classLoader.getResourceAsStream(fileName) ?: error("Cannot find resource")
    val reader = InputStreamReader(stream)
    return reader.readText()
}
