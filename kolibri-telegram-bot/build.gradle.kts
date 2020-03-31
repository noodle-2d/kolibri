dependencies {
    implementation(project(":kolibri-common"))
}

val mainClass = "com.ran.kolibri.telegram.bot.MainKt"

application {
    mainClassName = mainClass
}

tasks.withType<Jar>().all {
    manifest {
        attributes["Main-Class"] = mainClass
    }
}
