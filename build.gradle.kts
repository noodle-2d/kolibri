plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.50" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "idea")

    if (name == "kolibri-commandline-utility" || name == "kolibri-telegram-bot") {
        apply(plugin = "application")
    } else {
        apply(plugin = "java-library")
    }

    version = "1.0"

    val kotlinVersion = "1.3.50"

    repositories {
        jcenter()
    }

    dependencies {
        "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

        "testImplementation"("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    }
}
