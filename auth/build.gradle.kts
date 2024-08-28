group = "net.treset.mcdl.auth"

val gson: String by project
val msal: String by project

dependencies {
    implementation(project(":"))
    implementation("com.microsoft.azure:msal4j:$msal")
    implementation("com.google.code.gson:gson:$gson")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

ext["includedDependencies"] = listOf("msal4j")