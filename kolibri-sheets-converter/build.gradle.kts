extra["main-class-name"] = "com.ran.kolibri.sheets.converter.MainKt"

dependencies {
    implementation(project(":kolibri-common"))
}

application {
    mainClassName = extra["main-class-name"] as String
}
