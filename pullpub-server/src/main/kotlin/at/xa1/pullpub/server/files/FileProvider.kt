package at.xa1.pullpub.server.files

import java.io.File

interface FileProvider {
    fun getForPath(path: String): File?
}
