/*
 * Copyright (c) 2024 Lunabee Studio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * lunabee.library-publish-conventions.gradle.kts
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 1/25/2024 - for the Lunabee Compose library.
 */

import com.android.build.gradle.LibraryExtension
import org.gradle.configurationcache.extensions.capitalized
import studio.lunabee.library.SnapshotTask
import studio.lunabee.library.VersionTask
import java.net.URI

enum class PublishType {
    Android, Java
}

plugins {
    `maven-publish`
    signing
}

val publishType: PublishType = when {
    project.plugins.hasPlugin("android-library") -> PublishType.Android
    project.plugins.hasPlugin("java-library") -> PublishType.Java
    else -> error("Cannot determine the type of publication")
}

println("Configure publish of ${project.name} with type $publishType")

project.extensions.configure<PublishingExtension>("publishing") {
    setupPublication()
    afterEvaluate {
        setupMavenRepository()
    }
}

/**
 * Set repository destination depending on [project] and version name.
 * Credentials should be stored in your root gradle.properties, in a non source controlled file.
 */
fun PublishingExtension.setupMavenRepository() {
    repositories {
        maven {
            authentication {
                credentials.username = project.properties["sonatypeUsername"]?.toString()
                    ?: System.getenv("SONATYPE_USERNAME")
                credentials.password = project.properties["sonatypePassword"]?.toString()
                    ?: System.getenv("SONATYPE_PASSWORD")
            }
            url = URI.create(properties["publishing.url"]?.toString() ?: "https://s01.oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}

/**
 * Entry point for setting publication detail.
 */
fun PublishingExtension.setupPublication() {
    publications {
        when (publishType) {
            PublishType.Android -> create<MavenPublication>(project.name) {
                afterEvaluate { // version is set in project, so use after evaluate
                    setProjectDetails()
                    setAndroidArtifacts()
                    setPom()
                }
            }
            PublishType.Java -> create<MavenPublication>(project.name) {
                afterEvaluate { // version is set in project, so use after evaluate
                    setProjectDetails()
                    setJavaArtifacts()
                    setPom()
                }
            }
        }
    }
}

/**
 * Set project details:
 * - groupId will be [AndroidConfig.GROUP_ID]
 * - artifactId will take the name of the current [project]
 * - version will be set in each submodule gradle file
 */
fun MavenPublication.setProjectDetails() {
    groupId = AndroidConfig.GROUP_ID
    artifactId = project.name
    version = project.version.toString()
}

/**
 * Set POM file details.
 */
fun MavenPublication.setPom() {
    pom {
        name.set(project.name.capitalized())
        description.set(project.description)
        url.set(AndroidConfig.LIBRARY_URL)

        organization {
            name.set("Lunabee Studio")
            url.set("https://www.lunabee.studio")
        }

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        scm {
            connection.set("git@github.com:LunabeeStudio/Lunabee_Compose_Android.git")
            developerConnection.set("git@github.com:LunabeeStudio/Lunabee_Compose_Android.git")
            url.set(AndroidConfig.LIBRARY_URL)
        }

        developers {
            developer {
                id.set("Publisher")
                name.set("Publisher Lunabee")
                email.set("publisher@lunabee.com")
            }
        }

        withXml {
            asNode().appendNode("dependencies").apply {
                fun Dependency.write(scope: String) = appendNode("dependency").apply {
                    appendNode("groupId", group)
                    appendNode("artifactId", name)
                    version?.let { appendNode("version", version) }
                    appendNode("scope", scope)
                }

                configurations.findByName("api")?.dependencies?.forEach { dependency ->
                    dependency.write("implementation")
                }

                configurations.findByName("implementation")?.dependencies?.forEach { dependency ->
                    dependency.write("runtime")
                }
            }
        }
    }
}

private val Project.android: LibraryExtension
    get() = (this as ExtensionAware).extensions.getByName("android") as LibraryExtension

/**
 * Set additional artifacts to upload
 * - sources
 * - javadoc
 * - aar
 */
fun MavenPublication.setAndroidArtifacts() {
    val sourceJar by project.tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(project.android.sourceSets.getByName("main").java.srcDirs)
    }

    val javadocJar by project.tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(project.android.sourceSets.getByName("main").java.srcDirs)
    }

    artifact(sourceJar)
    artifact(javadocJar)
    val aarBasePath = project.layout.buildDirectory.dir("outputs/aar").get().asFile.path
    val filename = "${project.name.lowercase()}-release.aar"
    artifact("$aarBasePath/$filename")

    project.afterEvaluate {
        project.tasks.named("sign${project.name.capitalized()}Publication") {
            dependsOn("bundleReleaseAar")
        }
    }
}

/**
 * Set additional artifacts to upload
 * - sources
 * - javadoc
 * - jar
 */
fun MavenPublication.setJavaArtifacts() {
    artifact(project.layout.buildDirectory.dir("libs/${project.name}-${project.version}.jar").get().asFile)
    artifact(project.tasks.named("sourcesJar"))
    artifact(project.tasks.named("javadocJar"))

    project.afterEvaluate {
        project.tasks.named("sign${project.name.capitalized()}Publication") {
            dependsOn("jar")
        }
    }
}

signing {
    sign(publishing.publications[project.name])
}

afterEvaluate {
    tasks.withType(PublishToMavenRepository::class.java) {
        when (publishType) {
            PublishType.Android -> dependsOn(
                tasks.named("bundleReleaseAar"),
            )
            PublishType.Java -> dependsOn(
                tasks.withType(Jar::class.java),
            )
        }
    }
}

tasks.register("${project.name}Version", VersionTask::class.java)
tasks.register("${project.name}setSnapshotVersion", SnapshotTask::class.java)
