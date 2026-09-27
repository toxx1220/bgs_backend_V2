plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    idea
    alias(libs.plugins.nemerosa.versioning)
}

idea {
    module {
        // Exclude these directories from IntelliJ's project view and indexing
        excludeDirs = excludeDirs + setOf(file(".direnv"), file(".jdk"), file("build"), file(".gradle"))
    }
}

val projectVersion: String by project
val javaVersion: String by project
val projectGroup: String by project

val baseVersion: String =
    versioning.info.tag?.removePrefix("v")
        ?: "${versioning.info.branch}.${versioning.info.commit.take(7)}"

group = projectGroup
version =
    if (versioning.info.tag != null) {
        baseVersion
    } else {
        "$baseVersion-SNAPSHOT"
    }
extra["appVersion"] = baseVersion

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion.toInt())
    }
}

val generatedSourcesDir = file("build/generated/source/kapt/main")

sourceSets {
    main {
        // Kapt applies to main by default
    }
    test {
        java {
            srcDir(generatedSourcesDir)
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(libs.springdoc)
    implementation(libs.jgit)
    implementation(libs.kotlin.logging)
    implementation(libs.commons.csv)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jsoup)

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")

    // Kapt dependencies
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    kapt(libs.hibernate.processor)
    kaptTest(libs.hibernate.processor)

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-resttestclient")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.bootJar {
    archiveBaseName.set("bgs")
}

springBoot {
    buildInfo {
        properties {
            additional.set(mapOf("version" to (project.extra["appVersion"] as String)))
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.clean {
    delete("build/generated")
}
