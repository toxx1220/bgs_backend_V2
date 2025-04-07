pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        id("com.google.devtools.ksp") version "2.1.20-1.0.32" apply false
    }
}

rootProject.name = "bgs"
