import java.util.*

plugins {
    java
    `maven-publish`
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    afterEvaluate {

        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = rootProject.group.toString()
                    artifactId = "${rootProject.name}${if (rootProject.name == project.name) "" else "-${project.name}"}"
                    version = project.version.toString()

                    pom {
                        val humanName =
                            project.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                        name.set("MC Data Loader - $humanName")
                        description.set("The ${humanName} module for the MC Data Loader library.")
                        url.set("https://www.github.com/Tre5et/mcdl")

                        licenses {
                            license {
                                name.set("MIT License")
                                url.set("http://www.opensource.org/licenses/mit-license.php")
                            }
                        }

                        developers {
                            developer {
                                name.set("TreSet")
                                url.set("https://www.github.com/Tre5et")
                            }
                        }

                        scm {
                            connection.set("scm:git:git://github.com/Tre5et/mcdl.git")
                            developerConnection.set("scm:git:ssh://github.com/Tre5et/mcdl.git")
                            url.set("https://github.com/Tre5et/mcdl/tree/main")
                        }
                    }

                    from(components["java"])
                }
            }
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}

subprojects {
    afterEvaluate {
        val gson: String by project
        dependencies {
            implementation(project(":"))
            implementation("com.google.code.gson:gson:$gson")
            testImplementation(platform("org.junit:junit-bom:5.10.0"))
            testImplementation("org.junit.jupiter:junit-jupiter")
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