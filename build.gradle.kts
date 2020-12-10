import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61" apply false
    id("com.github.johnrengelman.shadow") version "4.0.4" apply false
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1" apply false
    id("org.jlleitschuh.gradle.ktlint-idea") version "9.2.1" apply false
}

val kotlinVersion = "1.3.61"
val ktorVersion = "1.3.2"
val typesafeVersion = "1.4.0"
val kodeinVersion = "4.1.0"
val postgresqlJdbcDriverVersion = "42.2.14"
val exposedVersion = "0.24.1"
val googleApiVersion = "1.30.4"
val googleSheetsApiVersion = "v4-rev581-1.25.0"
val slf4jVersion = "1.7.30"
val logbackVersion = "1.2.3"

allprojects {
    version = "1.0"

    repositories {
        jcenter()
    }

    apply(plugin = "idea")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jlleitschuh.gradle.ktlint-idea")
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

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

        "implementation"("org.postgresql:postgresql:$postgresqlJdbcDriverVersion")

        "implementation"("org.jetbrains.exposed:exposed-core:$exposedVersion")
        "implementation"("org.jetbrains.exposed:exposed-dao:$exposedVersion")
        "implementation"("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
        "implementation"("org.jetbrains.exposed:exposed-jodatime:$exposedVersion")

        "implementation"("com.google.api-client:google-api-client:$googleApiVersion")
        "implementation"("com.google.oauth-client:google-oauth-client-jetty:$googleApiVersion")
        "implementation"("com.google.apis:google-api-services-sheets:$googleSheetsApiVersion")

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
        setOf("kolibri-api", "kolibri-scheduler").contains(name)
