plugins {
    id("java")
}

group = "net.treset.mcdl"
version = "7.0.0"

allprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}
dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}