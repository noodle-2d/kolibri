import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61" apply false
}

val kotlinVersion = "1.3.61"
val ktorVersion = "1.3.2"
val typesafeVersion = "1.4.0"
val kodeinVersion = "4.1.0"
val slf4jVersion = "1.7.30"
val logbackVersion = "1.2.3"

subprojects {
    version = "1.0"

    repositories {
        jcenter()
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "idea")

    if (isApplicationModule(name)) {
        apply(plugin = "application")
    } else {
        apply(plugin = "java-library")
    }

    dependencies {
        "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
        "implementation"("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

        "implementation"("io.ktor:ktor-server-core:$ktorVersion")
        "implementation"("io.ktor:ktor-server-netty:$ktorVersion")
        "implementation"("io.ktor:ktor-client-cio:$ktorVersion")
        "implementation"("io.ktor:ktor-client-json:$ktorVersion")
        "implementation"("io.ktor:ktor-client-jackson:$ktorVersion")
        "implementation"("io.ktor:ktor-jackson:$ktorVersion")

        "implementation"("com.typesafe:config:$typesafeVersion")

        "implementation"("com.github.salomonbrys.kodein:kodein:$kodeinVersion")
        "implementation"("com.github.salomonbrys.kodein:kodein-conf:$kodeinVersion")

        "implementation"("org.slf4j:slf4j-api:$slf4jVersion")
        "implementation"("ch.qos.logback:logback-classic:$logbackVersion")
        "implementation"("ch.qos.logback:logback-core:$logbackVersion")

        "testImplementation"("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = "1.8"
    }
}

fun isApplicationModule(moduleName: String): Boolean =
    setOf("kolibri-commandline-utility", "kolibri-telegram-bot").contains(moduleName)
