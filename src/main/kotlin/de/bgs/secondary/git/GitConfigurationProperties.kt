package de.bgs.secondary.git

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "git")
data class GitConfigurationProperties(
    val repoUrl: String = "",
    val repoFolder: String = "",
    val gitToken: String = "",
    val boardGameCsvFileName: String = "",
    val gameFamilyCsvFileName: String = "",
)