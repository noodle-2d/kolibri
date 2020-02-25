import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

plugins {
    kotlin("jvm") version "1.3.61" apply false
    kotlin("plugin.allopen") version "1.3.61" apply false

    id("org.springframework.boot") version "2.0.5.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.7.RELEASE" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "idea")

    if (name == "kolibri-commandline-utility" || name == "kolibri-telegram-bot") {
        apply(plugin = "org.springframework.boot")
        apply(plugin = "application")
    } else {
        apply(plugin = "java-library")
    }

    version = "1.0"

    val kotlinVersion = "1.3.61"
    val springBootVersion = "2.0.5.RELEASE"

    repositories {
        jcenter()
    }

    dependencies {
        "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
        "implementation"("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        "implementation"("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        "implementation"("org.springframework.boot:spring-boot-starter-web")

        "testImplementation"("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
    }

    extensions.configure<AllOpenExtension>("allOpen") {
        annotation("org.springframework.context.annotation.Configuration")
        annotation("org.springframework.stereotype.Component")
    }
}
