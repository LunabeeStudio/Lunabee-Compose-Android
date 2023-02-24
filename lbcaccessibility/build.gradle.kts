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
 * Created by Lunabee Studio / Date - 5/9/2022 - for the Lunabee Compose library.
 */
import studio.lunabee.library.setPublication
import studio.lunabee.library.setRepository

plugins {
    id("studio.lunabee.library.android")
}

android {
    resourcePrefix("lbc_acc_")
    namespace = "studio.lunabee.compose.accessibility"
}

description = "A set of methods and composable for accessibility"
version = AndroidConfig.LBC_ACCESSIBILITY_VERSION

publishing {
    setRepository(project)
    setPublication(project)
}

signing {
    sign(publishing.publications[project.name])
}

dependencies {
    implementation(AndroidX.compose.foundation)
}
