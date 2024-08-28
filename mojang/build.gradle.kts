plugins {
    id("java")
}

group = "net.treset.mcdl.mojang"

repositories {
    mavenCentral()
}

val gson: String by project

dependencies {
    implementation(project(":"))
    implementation("com.google.code.gson:gson:$gson")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}