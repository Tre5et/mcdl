group = "net.treset.mcdl.resourcepacks"

val gson: String by project
val nbt: String by project

dependencies {
    implementation(project(":"))
    implementation("com.google.code.gson:gson:$gson")
    implementation("com.github.Querz:NBT:$nbt")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

ext["includedDependencies"] = listOf("nbt")