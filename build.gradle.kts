import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

plugins {
    kotlin("jvm") version "1.3.61" apply false
    kotlin("plugin.allopen") version "1.3.61" apply false

    id("org.springframework.boot") version "2.0.5.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.7.RELEASE" apply false
}

val kotlinVersion = "1.3.61"
val springBootVersion = "2.0.5.RELEASE"
val log4jVersion = "1.2.17"

subprojects {
    version = "1.0"

    repositories {
        jcenter()
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "idea")

    if (isApplicationModule(name)) {
        apply(plugin = "io.spring.dependency-management")
        apply(plugin = "org.springframework.boot")
        apply(plugin = "application")

        dependencies {
            "implementation"("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }
    } else {
        apply(plugin = "java-library")
    }

    dependencies {
        "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
        "implementation"("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        "implementation"("log4j:log4j:$log4jVersion")

        "testImplementation"("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    }

    extensions.configure<AllOpenExtension>("allOpen") {
        annotation("org.springframework.context.annotation.Configuration")
        annotation("org.springframework.stereotype.Component")
    }
}

fun isApplicationModule(moduleName: String): Boolean =
    setOf("kolibri-commandline-utility", "kolibri-telegram-bot").contains(moduleName)
