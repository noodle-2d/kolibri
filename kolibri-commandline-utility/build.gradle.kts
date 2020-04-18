extra["main-class-name"] = "com.ran.kolibri.commandline.utility.MainKt"

dependencies {
    implementation(project(":kolibri-common"))
}

application {
    mainClassName = extra["main-class-name"] as String
}
