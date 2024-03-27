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
 * LbcThemeUtilities.kt
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 10/21/2022 - for the Lunabee Compose library.
 */

package studio.lunabee.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import material.DynamicScheme
import material.Hct
import material.MaterialDynamicColors
import material.SchemeTonalSpot
import material.TonalPalette
import material.Variant

object LbcThemeUtilities {
    /**
     * Generate a [ColorScheme] ready to use with a [androidx.compose.material3.MaterialTheme].
     * @param color base primary color. All colors will be generated from this one.
     * @param isInDarkMode
     */
    @JvmStatic
    fun getMaterialColorSchemeFromColor(
        color: Color,
        isInDarkMode: Boolean,
    ): ColorScheme {
        val sourceColorHct = Hct.fromInt(color.toArgb())
        val schemeTonalSpot = if (isInDarkMode) {
            SchemeTonalSpot(sourceColorHct, true, 0.0)
        } else {
            SchemeTonalSpot(sourceColorHct, false, 0.0)
        }
        return schemeToColorScheme(
            scheme = DynamicScheme(
                sourceColorHct,
                Variant.NEUTRAL,
                isInDarkMode,
                0.0,
                schemeTonalSpot.primaryPalette,
                schemeTonalSpot.secondaryPalette,
                schemeTonalSpot.tertiaryPalette,
                schemeTonalSpot.neutralPalette,
                schemeTonalSpot.neutralVariantPalette,
            ),
        )
    }

    /**
     * Get color variant from a base color and the desired tone.
     * @param color base color
     * @param tone value between 0 and 100 representing your tone.
     */
    @JvmStatic
    fun getToneForColor(color: Color, tone: Int): Color {
        assert(value = tone in 0..100, lazyMessage = { "Tone value must be in range 0..100" })
        return Color(color = TonalPalette.fromInt(color.toArgb()).tone(tone))
    }

    private fun schemeToColorScheme(scheme: DynamicScheme): ColorScheme {
        val dynamicColors = MaterialDynamicColors()
        return ColorScheme(
            primary = Color(color = dynamicColors.primary().getArgb(scheme)),
            onPrimary = Color(color = dynamicColors.onPrimary().getArgb(scheme)),
            primaryContainer = Color(color = dynamicColors.primaryContainer().getArgb(scheme)),
            onPrimaryContainer = Color(color = dynamicColors.onPrimaryContainer().getArgb(scheme)),
            inversePrimary = Color(color = dynamicColors.inversePrimary().getArgb(scheme)),
            secondary = Color(color = dynamicColors.secondary().getArgb(scheme)),
            onSecondary = Color(color = dynamicColors.onSecondary().getArgb(scheme)),
            secondaryContainer = Color(color = dynamicColors.secondaryContainer().getArgb(scheme)),
            onSecondaryContainer = Color(color = dynamicColors.onSecondaryContainer().getArgb(scheme)),
            tertiary = Color(color = dynamicColors.tertiary().getArgb(scheme)),
            onTertiary = Color(color = dynamicColors.onTertiary().getArgb(scheme)),
            tertiaryContainer = Color(color = dynamicColors.tertiaryContainer().getArgb(scheme)),
            onTertiaryContainer = Color(color = dynamicColors.onTertiaryContainer().getArgb(scheme)),
            background = Color(color = dynamicColors.background().getArgb(scheme)),
            onBackground = Color(color = dynamicColors.onBackground().getArgb(scheme)),
            surface = Color(color = dynamicColors.surface().getArgb(scheme)),
            onSurface = Color(color = dynamicColors.onSurface().getArgb(scheme)),
            surfaceTint = Color(color = dynamicColors.primary().getArgb(scheme)),
            surfaceVariant = Color(color = dynamicColors.surfaceVariant().getArgb(scheme)),
            onSurfaceVariant = Color(color = dynamicColors.onSurfaceVariant().getArgb(scheme)),
            inverseSurface = Color(color = dynamicColors.inverseSurface().getArgb(scheme)),
            inverseOnSurface = Color(color = dynamicColors.inverseOnSurface().getArgb(scheme)),
            error = Color(color = dynamicColors.error().getArgb(scheme)),
            onError = Color(color = dynamicColors.onError().getArgb(scheme)),
            errorContainer = Color(color = dynamicColors.errorContainer().getArgb(scheme)),
            onErrorContainer = Color(color = dynamicColors.onErrorContainer().getArgb(scheme)),
            outline = Color(color = dynamicColors.outline().getArgb(scheme)),
            outlineVariant = Color(color = dynamicColors.outlineVariant().getArgb(scheme)),
            scrim = Color(color = dynamicColors.scrim().getArgb(scheme)),
            surfaceBright = Color(color = dynamicColors.surfaceBright().getArgb(scheme)),
            surfaceDim = Color(color = dynamicColors.surfaceDim().getArgb(scheme)),
            surfaceContainer = Color(color = dynamicColors.surfaceContainer().getArgb(scheme)),
            surfaceContainerHigh = Color(color = dynamicColors.surfaceContainerHigh().getArgb(scheme)),
            surfaceContainerHighest = Color(color = dynamicColors.surfaceContainerHighest().getArgb(scheme)),
            surfaceContainerLow = Color(color = dynamicColors.surfaceContainerLow().getArgb(scheme)),
            surfaceContainerLowest = Color(color = dynamicColors.surfaceContainerLowest().getArgb(scheme)),
        )
    }
}
