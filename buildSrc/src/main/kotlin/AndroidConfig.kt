import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
 * AndroidConfig.kt
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 4/8/2022 - for the Lunabee Compose library.
 */

object AndroidConfig {
    const val VERSION_CODE: Int = 1
    const val VERSION_NAME: String = BuildConfigs.lunabeeCompose

    const val COMPILE_SDK: Int = BuildConfigs.compileSdk
    const val TARGET_SDK: Int = COMPILE_SDK
    const val MIN_SDK: Int = BuildConfigs.minSdk
    const val BUILD_TOOLS_VERSION: String = "34.0.0"

    const val LIBRARY_URL: String = "https://github.com/LunabeeStudio/Lunabee_Compose_Android"
    const val GROUP_ID: String = "studio.lunabee.compose"

    // ⚠️ Match module name in UPPER_CASE ('-' -> '_')
    const val LBCCORE_VERSION: String = "1.6.0"
    const val LBCFOUNDATION_VERSION: String = "1.5.0"
    const val LBCANDROIDTEST_VERSION: String = "1.6.0"
    const val LBCACCESSIBILITY_VERSION: String = "1.9.0"
    const val LBCTHEME_VERSION: String = "1.5.0"
    const val MATERIAL_COLOR_UTILITIES_VERSION: String = "1.5.0"
    const val LBCHAPTIC_VERSION: String = "1.3.0"
    const val LBCUIFIELD_VERSION: String = "1.2.0"
    const val LBCIMAGE_VERSION: String = "1.2.0"
    const val LBCGLANCE_VERSION: String = "1.1.0"
    const val LBCPRESENTER_VERSION: String = "1.0.2-cmp-testing"

    const val LBCCORE_CMP_VERSION: String = "0.0.1-cmp-testing"
    const val LBCIMAGE_CMP_VERSION: String = "0.0.1-cmp-testing"

    val JDK_VERSION: JavaVersion = JavaVersion.VERSION_17
    val JVM_TARGET: JvmTarget = JvmTarget.JVM_17
}
