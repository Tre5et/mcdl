import io.gitee.pkmer.enums.PublishingType
import java.util.*

plugins {
    java
    `maven-publish`
    signing
    id("io.gitee.pkmer.pkmerboot-central-publisher") version "1.1.1"
}

val isRelease = project.version.toString().all { it.isDigit() || it == '.' }
val stagingDir = if(isRelease) {
    layout.buildDirectory.dir("target/staging-deploy/${project.version}")
} else {
    layout.buildDirectory.dir("target/snapshot/${project.version}")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

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
            repositories {
                maven {
                    name = "Local"
                    setUrl(stagingDir)
                }
            }

            publications {
                create<MavenPublication>("maven") {

                    groupId = rootProject.group.toString()
                    artifactId = "${rootProject.name}${if (rootProject.name == project.name) "" else "-${project.name}"}"
                    version = project.version.toString()

                    pom {
                        val humanName = if(project.name == rootProject.name) {
                            "Base"
                        } else {
                            project.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                        }

                        name.set("MC Data Loader - $humanName")
                        description.set("The ${humanName} module for the MC Data Loader library.")
                        url.set("https://www.github.com/Tre5et/mcdl")

                        licenses {
                            license {
                                name.set("GNU Lesser General Public License v3.0")
                                url.set("https://www.gnu.org/licenses/lgpl-3.0.en.html")
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

    signing {
        if(isRelease) {
            sign(publishing.publications)
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

pkmerBoot {
    sonatypeMavenCentral {
        stagingRepository = stagingDir
        username = (findProperty("ossrhUsername") ?: "") as String
        password = (findProperty("ossrhPassword") ?: "") as String

        publishingType = PublishingType.USER_MANAGED
    }
}

tasks.getByPath("bundleForUpload").apply {
    subprojects.forEach { dependsOn("${it.name}:publish") }
}

tasks.getByPath("publishToMavenCentralPortal").apply {
    doFirst {
        if (!isRelease) {
            throw IllegalStateException("Refusing to publish non-release version to Maven Central")
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