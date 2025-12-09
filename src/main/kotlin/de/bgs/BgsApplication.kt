package de.bgs

import de.bgs.secondary.git.GitConfigurationProperties
import de.bgs.secondary.metadata.MetaDataConfigurationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(GitConfigurationProperties::class, MetaDataConfigurationProperties::class)
class BgsApplication

fun main(args: Array<String>) {
    runApplication<BgsApplication>(*args)
}
