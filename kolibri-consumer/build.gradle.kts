extra["main-class-name"] = "com.ran.kolibri.consumer.MainKt"

dependencies {
    implementation(project(":kolibri-common"))
}

application {
    mainClassName = extra["main-class-name"] as String
}
