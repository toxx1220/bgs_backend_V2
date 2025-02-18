package de.bgs.secondary.git

import io.github.oshai.kotlinlogging.KotlinLogging
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.util.*


@Service
class GitService(gitProperties: GitConfigurationProperties) {
    val logger = KotlinLogging.logger {}

    private val dataDirectory: File = getOrMakeFile(gitProperties.repoRoot)
    private val repoUrl = gitProperties.repoUrl
    private val credentialsProvider = UsernamePasswordCredentialsProvider("token", gitProperties.gitToken)
    var repository: Repository? = null

    fun pull(repo: Repository) {
        try {
            Git(repo).pull()
                .setCredentialsProvider(credentialsProvider)
                .call()
        } catch (e: GitAPIException) {
            logger.error { "Could not pull repository $repo ${repo.workTree}, due to ${e.printStackTrace()}" }
        }
    }

    /**
     * Either gets the existing repository if set, or tries to get it from the directory, and if successful sets it.
     */
    fun getRepository(): Optional<Repository> {
        try {
            repository = repository ?: Git.open(dataDirectory).repository
            return Optional.ofNullable(repository)
        } catch (e: IOException) {
            return Optional.empty()
        }
    }

    fun cloneGitRepository(): Optional<Repository> {

        try {
            Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(dataDirectory)
                .setCredentialsProvider(credentialsProvider)
                .setProgressMonitor(LoggingProgressMonitor())
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

    private fun getOrMakeFile(filePath: String): File {
        val file = File(filePath)
        if (file.exists() || file.mkdirs()) return file
        throw IOException("Could not create directory: $dataDirectory")
    }
}
