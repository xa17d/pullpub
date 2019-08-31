package at.xa1.pullpub.server

import at.xa1.pullpub.server.files.BaseFolderFileProvider
import at.xa1.pullpub.server.logging.createInMemoryLogging
import at.xa1.pullpub.server.repository.GitRepository
import at.xa1.shell.ProcessShell
import io.ktor.util.combineSafe
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    if (args.size != 5) {
        println("Expecting exactly 5 arguments:")
        println("Argument                       | Example")
        println("-------------------------------+-----------------------------------------------------")
        println("[0] port                       | 8080")
        println("[1] admin path                 | /my-secret-admin-url/")
        println("[2] repository folder          | /srv/repos/onlinerepo")
        println("[3] wwwroot path in repository | wwwroot")
        println("[4] repository clone URL       | https://github.com/xa17d/pullpub-website-example.git")
        exitProcess(-1)
    }

    val port = args[0].toInt()
    val adminPath = args[1]
    val repositoryFolderPath = args[2]
    val repositoryWwwRootPath = args[3]
    val repositoryCloneUrl = args[4]

    val logging = createInMemoryLogging()
    logging.logger.info(LOG_TAG, "Starting...")

    val repositoryFolder = File(repositoryFolderPath)
    val shell = ProcessShell(repositoryFolder)
    val git = GitRepository(shell)
    val fileProvider = BaseFolderFileProvider(repositoryFolder.combineSafe(repositoryWwwRootPath))

    runBlocking {
        if (!git.isInitialized()) {
            logging.logger.info(LOG_TAG, "Git is not initialized - cloning...")
            git.clone(repositoryCloneUrl)
        } else {
            logging.logger.info(LOG_TAG, "Git is already initialized")
        }
    }

    logging.logger.info(LOG_TAG, "Starting server...")

    Server(
        port,
        adminPath,
        logging,
        git,
        fileProvider
    ).startBlocking()
}

private val LOG_TAG = "Startup"
