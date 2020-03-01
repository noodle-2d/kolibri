val springVersion = "5.2.4.RELEASE"
val jacksonVersion = "2.10.2"

dependencies {
    implementation("org.springframework:spring-web:$springVersion")
    implementation("org.springframework:spring-context:$springVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
}
