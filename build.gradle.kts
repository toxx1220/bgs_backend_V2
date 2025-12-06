plugins {
    kotlin("jvm") version "2.3.0-RC2"
    kotlin("plugin.spring") version "2.3.0-RC"
    kotlin("plugin.jpa") version "2.3.0-RC"
    kotlin("kapt") version "2.3.0-RC"
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    idea
    id("net.nemerosa.versioning") version "3.1.0"
}

val baseVersion: String = versioning.info.tag?.removePrefix("v")
    ?: "${versioning.info.branch}.${versioning.info.commit.take(7)}"

group = "de"
version = if (versioning.info.tag != null) {
    baseVersion
} else {
    "$baseVersion-SNAPSHOT"
}
extra["appVersion"] = baseVersion

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
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
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.4.0.202509020913-r")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.13")
    implementation("org.apache.commons:commons-csv:1.14.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.2")
    implementation("io.projectreactor.netty:reactor-netty-http:1.3.0")
    implementation("org.jsoup:jsoup:1.21.2")


    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")

    // Kapt dependencies
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.hibernate.orm:hibernate-jpamodelgen:7.1.11.Final")
    kaptTest("org.hibernate.orm:hibernate-jpamodelgen:7.1.11.Final")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
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
