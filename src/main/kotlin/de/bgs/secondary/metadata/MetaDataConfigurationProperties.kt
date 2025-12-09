package de.bgs.secondary.metadata

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "metadata")
data class MetaDataConfigurationProperties (
    var bggApiKey: String = "",
)