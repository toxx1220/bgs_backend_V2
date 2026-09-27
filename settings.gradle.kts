pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("io.github.ben-manes.versions.settings") version "0.64.0"
}

val rootProjectName: String by settings
rootProject.name = rootProjectName
