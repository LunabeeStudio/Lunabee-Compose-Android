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
 * Dependencies.kt
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 4/8/2022 - for the Lunabee Compose library.
 */

object Modules {
    const val LbcAccessibility: String = ":lbcaccessibility"
    const val LbcFoundation: String = ":lbcfoundation"
    const val LbcTheme: String = ":lbctheme"
    const val LbcCore: String = ":lbccore"
    const val LbcCoreCmp: String = ":lbccore-cmp"
    const val LbcImage: String = ":lbcimage"
    const val LbcImageCmp: String = ":lbcimage-cmp"
    const val LbcAndroidTest: String = ":lbcandroidtest"
    const val MaterialColorUtilities: String = ":material-color-utilities"
    const val LbcHaptic: String = ":lbchaptic"
    const val LbcCrop: String = ":lbccrop"
    const val LbcUiField: String = ":lbcuifield"
    const val LbcGlance: String = ":lbcglance"
    const val LbcPresenter: String = ":lbcpresenter"
}

object BuildConfigs {
    const val lunabeeCompose: String = "_"
    const val compileSdk: Int = 35
    const val minSdk: Int = 21
    const val targetSdk: Int = 35
}
