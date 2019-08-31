package at.xa1.pullpub.server.files

import java.io.File

class MockFileProvider : FileProvider {

    val requestedPaths = mutableListOf<String>()
    var providedFile: File? = null

    fun provideHtml(html: String) {
        providedFile = File("temp-MockFileProvider.html").apply {
            writeText(html)
        }
    }


    override fun getForPath(path: String): File? {
        requestedPaths.add(path)
        return providedFile
    }

}
