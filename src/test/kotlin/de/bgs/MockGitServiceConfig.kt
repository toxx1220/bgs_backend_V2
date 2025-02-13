package de.bgs

import de.bgs.secondary.git.GitConfigurationProperties
import de.bgs.secondary.git.GitService
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class MockGitServiceConfig {
    @Bean
    @Primary
    fun gitService(gitProperties: GitConfigurationProperties): GitService {
        // Create a mock implementation that doesn't actually try to clone or access git
        return Mockito.mock(GitService::class.java)
    }
}