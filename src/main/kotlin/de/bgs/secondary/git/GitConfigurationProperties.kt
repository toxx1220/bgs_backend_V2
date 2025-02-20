package de.bgs.secondary.git

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "git")
data class GitConfigurationProperties(
    var schedulerEnabled: Boolean = false,
    var skipFirstExecution: Boolean = false,
    var repoUrl: String = "",
    var repoRoot: String = "",
    var gitToken: String = "",
    var boardGameCsvFileName: String = "",
    var gameFamilyCsvFileName: String = "",
    var gameTypeCsvFileName: String = "",
    var personCsvFileName: String = "",
    var categoryCsvFileName: String = "",
    var mechanicCsvFileName: String = "",
    var publisherCsvFileName: String = ""
)