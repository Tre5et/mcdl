val msal: String by project
dependencies {
    implementation("com.microsoft.azure:msal4j:$msal")
    implementation("com.google.guava:guava:33.3.1-jre")
}