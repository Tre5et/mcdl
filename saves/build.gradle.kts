plugins {
    id("java")
}

group = "net.treset.mcdl.saves"

val nbt: String by project

dependencies {
    implementation(project(":"))
    implementation("com.github.Querz:NBT:$nbt")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

ext["includedDependencies"] = listOf("nbt")

tasks.test {
    useJUnitPlatform()
}