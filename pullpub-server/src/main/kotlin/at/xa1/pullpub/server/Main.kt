package at.xa1.pullpub.server

import at.xa1.pullpub.server.files.BaseFolderFileProvider
import at.xa1.pullpub.server.logging.createInMemoryLogging
import at.xa1.pullpub.server.repository.GitRepository
import at.xa1.shell.ProcessShell
import java.io.File

fun main(args: Array<String>) {

    val folder = File(args[0])
    val shell = ProcessShell(folder)
    val git = GitRepository(shell)
    val fileProvider = BaseFolderFileProvider(folder)

    Server(
        8080,
        "/admin",
        createInMemoryLogging(),
        git,
        fileProvider
    ).startBlocking()
}
