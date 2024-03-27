/*
 * Copyright © 2022 Lunabee Studio
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
 * build.gradle.kts
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 4/8/2022 - for the Lunabee Compose library.
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.detekt)
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

detekt {
    parallel = true
    source.setFrom(files(rootProject.rootDir))
    buildUponDefaultConfig = true
    config.setFrom(files("$projectDir/lunabee-detekt-config.yml"))
    autoCorrect = true
    ignoreFailures = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    outputs.upToDateWhen { false }

    exclude("**/build/**")

    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("${layout.buildDirectory.asFile.get().path}/reports/detekt/detekt-report.xml"))

        html.required.set(true)
        html.outputLocation.set(file("${layout.buildDirectory.asFile.get().path}/reports/detekt/detekt-report.html"))
    }
}

tasks.create("publishList") {
    doLast {
        val publishProjects = project.allprojects.filter {
            it.tasks.findByName("publish") != null
        }.joinToString(";") { it.name }
        println(publishProjects)
    }
}