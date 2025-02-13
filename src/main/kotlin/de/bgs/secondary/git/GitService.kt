package de.bgs.secondary.git

import io.github.oshai.kotlinlogging.KotlinLogging
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.lib.TextProgressMonitor
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.system.exitProcess


@Service
class GitService(private val gitProperties: GitConfigurationProperties) {
    val logger = KotlinLogging.logger {}
    val loggerWriter = PrintWriter(object : StringWriter() {
        override fun flush() {
            logger.info { toString() }
            buffer.setLength(0)
        }
    })
    private val dataDirectory: File = File(gitProperties.repoRoot)
    val repository: Repository = getRepositoryFromPath()
        .orElseGet {
            cloneGitRepository()
                .orElseThrow {
                    logger.error { "Could not clone repository" }
                    exitProcess(69)
                }
        }

    init {
        if (!dataDirectory.exists()) if (!dataDirectory.mkdirs()) throw IOException("Could not create directory: $dataDirectory")
    }

    fun pull() {
        try {
            Git(repository).pull().call()
        } catch (e: GitAPIException) {
            logger.error { "Could not pull repository" }
        }
    }

    private fun getRepositoryFromPath(): Optional<Repository> {
        return try {
            Optional.ofNullable(Git.open(dataDirectory).repository)
        } catch (e: IOException) {
            Optional.empty()
        }
    }

    private fun cloneGitRepository(): Optional<Repository> {
        if (!dataDirectory.exists()) if (!dataDirectory.mkdirs()) throw IOException("Could not create directory: $dataDirectory")

        try {
            Git.cloneRepository()
                .setURI(gitProperties.repoUrl)
                .setDirectory(dataDirectory)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider("PRIVATE-TOKEN", gitProperties.gitToken))
                .setProgressMonitor(
                    TextProgressMonitor(loggerWriter)
                )
                .setDepth(1) // minimize total download size
                .setNoCheckout(true)
                .call().use { git ->
                    val config = git.repository.config
                    config.setBoolean("core", null, "sparseCheckout", true)

                    // Create sparse-checkout file with patterns
                    val sparseCheckoutFile = File(git.repository.directory, "info/sparse-checkout")
                    sparseCheckoutFile.parentFile.mkdirs()
                    sparseCheckoutFile.writeText(
                        """
                    # Only include these specific files/directories:
                    /scraped/*.csv
                """.trimIndent()
                    )

                    git.checkout()
                        .setName("main")
                        .call()

                    return Optional.ofNullable(git.repository)
                }
        } catch (e: GitAPIException) {
            return Optional.empty()
        }
    }

}
