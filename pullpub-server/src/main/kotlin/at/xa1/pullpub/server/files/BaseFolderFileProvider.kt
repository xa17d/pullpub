package at.xa1.pullpub.server.files

import io.ktor.util.combineSafe
import java.io.File

class BaseFolderFileProvider(
    private val baseFolder: File,
    private val defaultFilename: String = "index.html"
) : FileProvider {
    override fun getForPath(path: String): File? {
        val file = baseFolder.combineSafe(path)
        return when {
            file.isFile -> file
            file.isDirectory -> {
                val defaultFile = file.combineSafe(defaultFilename)
                if (defaultFile.isFile) {
                    defaultFile
                } else {
                    null
                }
            }
            else -> null
        }
    }
}
