plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

allprojects {
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}

group = "net.treset.mcdl"

val gson: String by project

dependencies {
    implementation("com.google.code.gson:gson:$gson")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

fun registerTask(
    name: String,
    dependencies: List<String> = listOf(),
    onRegister: Task.() -> Unit = {},
    onExecute: Task.() -> Unit = {}
) {
    tasks.register(name) {
        group = "mcdl"
        dependencies.forEach { dependsOn(it) }

        onRegister()

        doLast {
            onExecute()
        }
    }
}

subprojects {
    afterEvaluate {
        fun addDependency(dep: ResolvedDependency, from: (FileTree) -> Unit) {
            System.out.println("Adding dependency to jar: ${dep.name}")
            dep.moduleArtifacts.forEach {
                from(zipTree(it.file.absoluteFile))
            }
            dep.children.forEach {
                addDependency(it, from)
            }
        }

        tasks.jar {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE

            val includedDependencies: List<String>? by project
            includedDependencies?.let { includedDependencies ->
                System.out.println("Included dependencies for ${project.name}: $includedDependencies")
                val regex = "(${includedDependencies.joinToString("|").lowercase()})".toRegex()
                project.configurations["compileClasspath"].resolvedConfiguration.firstLevelModuleDependencies.forEach { dep: ResolvedDependency ->
                    if (dep.name.lowercase().contains(regex)) {
                        addDependency(dep, ::from)
                    } else {
                        System.out.println("Skipping dependency: ${dep.name}")
                    }
                }
            } ?: System.out.println("No included dependencies for ${project.name}")
        }
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

tasks.test {
    useJUnitPlatform()
}