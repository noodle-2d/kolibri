dependencies {
    implementation(project(":kolibri-common"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}

application {
    mainClassName = "com.ran.kolibri.telegram.bot.MainKt"
}
