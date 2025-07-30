plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.spring") version "2.2.0"
    kotlin("plugin.jpa") version "2.2.0"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("kapt") version "2.2.0"
    idea
}

group = "de"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
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
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.2.0.202503040940-r")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.7")
    implementation("org.apache.commons:commons-csv:1.14.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.2")
    implementation("io.projectreactor.netty:reactor-netty-http:1.2.8")
    implementation("org.jsoup:jsoup:1.20.1")


    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")

    // Kapt dependencies
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.hibernate.orm:hibernate-jpamodelgen")
    kaptTest("org.hibernate.orm:hibernate-jpamodelgen")

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

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.clean {
    delete("build/generated")
}
