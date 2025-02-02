package de.bgs.secondary.git

import de.bgs.core.UpdateService.Companion.REPO_URL_FORMAT_STRING
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.text.MessageFormat
import java.util.*


@Service
class GitService(@Value("\${GIT_TOKEN}") private val gitToken: String) {
    private val cloneUrl = MessageFormat.format(REPO_URL_FORMAT_STRING, gitToken)

    fun cloneGitRepository(filePath: File): Optional<Repository> {

        try {
            Git.cloneRepository()
                .setURI(cloneUrl)
                .setDirectory(filePath)
                .setCredentialsProvider(UsernamePasswordCredentialsProvider(gitToken, ""))
                .call().use { git ->
                    return Optional.ofNullable(git.repository)
                }
        } catch (e: GitAPIException) {
            return Optional.empty()
        }
    }
}