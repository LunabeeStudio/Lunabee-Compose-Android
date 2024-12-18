import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

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
 * build.gradle.kts
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 11/18/2024 - for the Lunabee Compose library.
 */

plugins {
    id("lunabee.multiplatform-library-conventions")
    id("lunabee.library-publish-conventions")
    alias(libs.plugins.compose)
}

android {
    resourcePrefix("lbc_presenter_")
    namespace = "studio.lunabee.compose.presenter"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

description = "Way to create screens in android replacing the MVVM pattern"
version = AndroidConfig.LBCPRESENTER_VERSION

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(libs.jetbrains.viewmodel)
        }

        val webMain by creating {
            dependsOn(commonMain.get())
        }

        wasmJsMain {
            dependsOn(webMain)
        }
        jsMain {
            dependsOn(webMain)
        }
    }
}
