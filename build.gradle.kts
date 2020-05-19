group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.3.70"
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.spring
    kotlin("plugin.spring") version "1.3.70"
    // https://plugins.gradle.org/plugin/org.springframework.boot
    id("org.springframework.boot") version "2.2.5.RELEASE"
    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    // Project Reactor
    implementation("io.projectreactor:reactor-core:3.3.3.RELEASE")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.0.2.RELEASE")

    // GraphQL
    implementation("com.graphql-java:graphql-java:14.0")
    implementation("com.graphql-java:graphql-java-extended-scalars:1.0")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}