@file:OptIn(ExperimentalWasmDsl::class, ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

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
 * Created by Lunabee Studio / Date - 11/19/2024 - for the Lunabee Compose library.
 */

plugins {
    id("lunabee.multiplatform-library-conventions")
    id("lunabee.library-publish-conventions")
    alias(libs.plugins.compose)
}

android {
    resourcePrefix("lbc_core_cmp_")
    namespace = "studio.lunabee.compose.core.cmp"
}
compose.resources {
    packageOfResClass = "studio.lunabee.compose.core.cmp.generated.resources"
}

description = "A set of tools for Compose Multiplatform"
version = AndroidConfig.LBCCORE_CMP_VERSION

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.foundation)
            api(compose.components.resources)
            api(libs.coil3)
            implementation(compose.components.uiToolingPreview)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
