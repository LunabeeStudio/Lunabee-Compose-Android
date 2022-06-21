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
 * OnClickDescription.kt
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 6/21/2022 - for the Lunabee Compose library.
 */

package studio.lunabee.compose.lbcaccessibility.model

/**
 * Use this class to set a click label for accessibility if you don't have access to Modifier's clickable method.
 *
 * @param action action to be executed on click
 * @param clickLabel label read by TalkBack
 */
data class OnClickDescription(
    val action: () -> Unit,
    val clickLabel: String,
)
