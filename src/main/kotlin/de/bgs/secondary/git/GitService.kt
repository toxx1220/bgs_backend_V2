package de.bgs.secondary.git

import de.bgs.core.UpdateService.Companion.REPO_URL
import io.github.oshai.kotlinlogging.KotlinLogging
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.lib.TextProgressMonitor
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*


@Service
class GitService(@Value("\${GIT_TOKEN}") private val gitToken: String) {
    val logger = KotlinLogging.logger {}

    fun cloneGitRepository(filePath: File): Optional<Repository> {
        if (!filePath.exists()) if (!filePath.mkdirs()) throw IOException("Could not create directory: $filePath")

        try {
            Git.cloneRepository()
                .setURI(REPO_URL)
                .setDirectory(filePath)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider("PRIVATE-TOKEN", gitToken))
                .setProgressMonitor(
                    TextProgressMonitor(
                        PrintWriter(
                            StringWriter().apply { logger.info { this.toString() } },
                            true
                        )
                    )
                )
                .call().use { git ->
                    return Optional.ofNullable(git.repository)
                }
        } catch (e: GitAPIException) {
            return Optional.empty()
        }
    }
}
