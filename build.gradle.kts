import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.61" apply false
    id("com.github.johnrengelman.shadow") version "4.0.4" apply false
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

    forApplications {
        apply(plugin = "application")
        apply(plugin = "com.github.johnrengelman.shadow")
    }

    forLibraries {
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

    forApplications {
        afterEvaluate {
            val mainClassName = extra["main-class-name"]

            tasks.named<DefaultTask>("build") {
                dependsOn(tasks.named("shadowJar"))
            }

            tasks.withType<ShadowJar>().all {
                archiveBaseName.set("${project.name}-shadow")
                mergeServiceFiles()
                manifest {
                    attributes["Main-Class"] = mainClassName
                }
            }
        }
    }
}

fun Project.forApplications(action: Project.() -> Unit) {
    if (isApplicationModule()) action(this)
}

fun Project.forLibraries(action: Project.() -> Unit) {
    if (!isApplicationModule()) action(this)
}

fun Project.isApplicationModule(): Boolean =
    setOf("kolibri-commandline-utility", "kolibri-telegram-bot").contains(name)
