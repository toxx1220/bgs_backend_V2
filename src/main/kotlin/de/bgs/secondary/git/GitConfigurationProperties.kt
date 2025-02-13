package de.bgs.secondary.git

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "git")
data class GitConfigurationProperties(
    var gitSchedulerEnabled: Boolean = false,
    var repoUrl: String = "",
    var repoRoot: String = "",
    var gitToken: String = "",
    var boardGameCsvFileName: String = "",
    var gameFamilyCsvFileName: String = "",
)