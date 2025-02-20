package de.bgs.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "conf")
data class ConfigurationProperties(
    var serverUrl: String = "http://localhost:8080"
)
