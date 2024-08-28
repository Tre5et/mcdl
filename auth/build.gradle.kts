plugins {
    id("java")
}

group = "net.treset.mcdl.auth"

repositories {
    mavenCentral()
}

val gson: String by project
val msal: String by project

dependencies {
    implementation(project(":"))
    implementation("com.microsoft.azure:msal4j:$msal")
    implementation("com.google.code.gson:gson:$gson")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

ext["includedDependencies"] = listOf("msal4j", "oauth2", "json-smart", "slf4j-api", )

tasks.test {
    useJUnitPlatform()
}