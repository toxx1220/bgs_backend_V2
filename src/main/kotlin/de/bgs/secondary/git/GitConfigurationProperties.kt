package de.bgs.secondary.git

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "git")
data class GitConfigurationProperties(
    var repoUrl: String = "",
    var repoFolder: String = "",
    var gitToken: String = "",
    var boardGameCsvFileName: String = "",
    var gameFamilyCsvFileName: String = "",
)