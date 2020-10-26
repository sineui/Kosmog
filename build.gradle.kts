group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"

    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.spring
    kotlin("plugin.spring") version "1.4.10"
    // https://plugins.gradle.org/plugin/org.springframework.boot
    id("org.springframework.boot") version "2.3.4.RELEASE"
    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    // https://github.com/jlleitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("org.jlleitschuh.gradle.ktlint-idea") version "9.4.1"
}

configurations {
    ktlint
}

repositories {
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Project Reactor
    implementation("io.projectreactor:reactor-core:3.3.10.RELEASE")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.0.2.RELEASE")

    // GraphQL
    implementation("com.graphql-java:graphql-java:15.0")
    implementation("com.graphql-java:graphql-java-extended-scalars:1.0.1")

    // jwt
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // ktlint
    implementation("org.jlleitschuh.gradle:ktlint-gradle:9.4.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
