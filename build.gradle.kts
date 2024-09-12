plugins {
    java
}

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

subprojects {
    group = "${rootProject.group}.${project.name}"

    afterEvaluate {
        val addDeps: List<String>? by project

        val gson: String by project
        dependencies {
            implementation(project(":"))
            implementation("com.google.code.gson:gson:$gson")
            addDeps?.forEach(::implementation)
            testImplementation(platform("org.junit:junit-bom:5.10.0"))
            testImplementation("org.junit.jupiter:junit-jupiter")
        }

        fun addDependency(dep: ResolvedDependency, from: (FileTree) -> Unit) {
            if(dep.name.startsWith("org.slf4j")) {
                System.out.println("Skipping dependency: ${dep.name}")
                return
            }
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

            addDeps?.let { addDeps ->
                System.out.println("Included dependencies for ${project.name}: $addDeps")
                val regex = "(${addDeps.joinToString("|").lowercase()})".toRegex()
                project.configurations["compileClasspath"].resolvedConfiguration.firstLevelModuleDependencies.forEach { dep: ResolvedDependency ->
                    if (dep.name.lowercase().contains(regex)) {
                        addDependency(dep, ::from)
                    } else {
                        System.out.println("Skipping dependency: ${dep.name}")
                    }
                }
            } ?: System.out.println("No included dependencies for ${project.name}")
        }

        tasks.build {
            doLast {
                val dir = File(rootProject.buildDir, "dist/${rootProject.version}/${rootProject.name}/${rootProject.name}-${project.name}/${project.version}").absoluteFile
                dir.mkdirs()
                val jar = File(project.buildDir, "libs/${project.name}-${project.version}.jar")
                val dest = File(dir, "${rootProject.name}-${project.name}-${project.version}.jar")
                System.out.println("Copying jar from ${project.name} jar to $dest")
                jar.copyTo(dest, true)

                System.out.println("Creating maven pom file")
                val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>net.treset.${rootProject.name}</groupId>\n" +
                        "  <artifactId>${rootProject.name}-${project.name}</artifactId>\n" +
                        "  <version>${project.version}</version>\n" +
                        "</project>"
                val pom = File(dir, "${rootProject.name}-${project.name}-${project.version}.pom")
                pom.writeText(xml)
            }
        }
    }
}

group = "net.treset.mcdl"

val gson: String by project
dependencies {
    implementation("com.google.code.gson:gson:$gson")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

tasks.build {
    doLast {
        val dir = File(rootProject.buildDir, "dist/${rootProject.version}/${rootProject.name}/${project.name}/${project.version}").absoluteFile
        dir.mkdirs()
        val jar = File(project.buildDir, "libs/${project.name}-${project.version}.jar")
        val dest = File(dir, "${project.name}-${project.version}.jar")
        System.out.println("Copying jar from root project jar to $dest")
        jar.copyTo(dest, true)

        System.out.println("Creating maven pom file")
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <groupId>net.treset.${rootProject.name}</groupId>\n" +
                "  <artifactId>${project.name}</artifactId>\n" +
                "  <version>${project.version}</version>\n" +
                "</project>"
        val pom = File(dir, "${project.name}-${project.version}.pom")
        pom.writeText(xml)
    }
}