import io.gitee.pkmer.enums.PublishingType
import java.util.*

plugins {
    java
    `maven-publish`
    signing
    id("io.gitee.pkmer.pkmerboot-central-publisher") version "1.1.1"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "io.gitee.pkmer.pkmerboot-central-publisher")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    val isRelease = project.version.toString().all { it.isDigit() || it == '.' }

    afterEvaluate {
        publishing {
            repositories {
                maven {
                    name = "Local"
                    setUrl(layout.buildDirectory.dir("target/staging-deploy"))
                }
            }

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
                            url.set("https://github.com/Tre5et/mcdl")
                        }
                    }

                    from(components["java"])
                }
            }
        }
    }

    pkmerBoot {
        sonatypeMavenCentral {
            if(!isRelease) {
                throw IllegalStateException("Cannot publish non-release version to Maven Central")
            }

            stagingRepository = layout.buildDirectory.dir("target/staging-deploy")
            username = findProperty("ossrhUsername") as String
            password = findProperty("ossrhPassword") as String

            publishingType = PublishingType.USER_MANAGED
        }
    }

    signing {
        sign(publishing.publications)
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

group = "dev.treset.mcdl"

val gson: String by project
dependencies {
    implementation("com.google.code.gson:gson:$gson")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}