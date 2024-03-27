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
 * ThemeScreen.kt
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 11/30/2022 - for the Lunabee Compose library.
 */

package studio.lunabee.compose.demo.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import studio.lunabee.compose.R
import studio.lunabee.compose.common.AppDemoTheme
import studio.lunabee.compose.theme.LbcThemeUtilities
import java.util.Locale

@Composable
fun ThemeScreen() {
    var colorHex: String by rememberSaveable { mutableStateOf(value = "") }
    val customColor = try {
        Color(android.graphics.Color.parseColor("#${colorHex.replace("#", "")}"))
    } catch (e: Exception) {
        null
    }

    val customColorScheme = customColor?.let {
        LbcThemeUtilities.getMaterialColorSchemeFromColor(it, isInDarkMode = isSystemInDarkTheme())
    }
    AppDemoTheme(
        customColorScheme = customColorScheme,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
            ) {
                OutlinedTextField(
                    value = colorHex,
                    onValueChange = { colorHex = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    label = { Text(text = stringResource(id = R.string.material3_theme_color_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.material3_theme_color_placeholder)) },
                )

                Spacer(modifier = Modifier.width(16.dp))

                customColor?.let {
                    BoxWithColorHex(
                        color = customColor,
                        modifier = Modifier.padding(top = 8.dp),
                        name = "Seed",
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }

                BoxWithColorHex(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp),
                    name = "Primary",
                )
            }

            val colorMap = getPalette(customColor, MaterialTheme.colorScheme)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                item {
                    Text(
                        text = stringResource(id = R.string.material3_theme_primary_explanation),
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                    )
                }

                items(
                    items = colorMap,
                ) { (colorName, color) ->
                    Text(
                        text = colorName,
                        modifier = Modifier
                            .padding(all = 16.dp),
                        style = MaterialTheme.typography.titleLarge,
                    )

                    LazyRow {
                        item {
                            BoxWithColorHex(
                                color = color,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(all = 8.dp),
                            )
                        }

                        for (i in 5 until 100 step 5) {
                            item {
                                val tone = LbcThemeUtilities.getToneForColor(color = color, tone = i)

                                BoxWithColorHex(
                                    color = tone,
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(all = 8.dp),
                                ) {
                                    Text(
                                        text = (100 - i).toString(),
                                        color = if (tone.luminance() >= 0.5) Color.Black else Color.White,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoxWithColorHex(
    color: Color,
    modifier: Modifier = Modifier,
    name: String? = null,
    innerText: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = RoundedCornerShape(size = 4.dp))
                .drawBehind { drawRect(color) },
            contentAlignment = Alignment.Center,
        ) {
            innerText?.invoke()
        }

        val colorText = buildString {
            append(color.hexValue)
            if (name != null) {
                appendLine()
                append(name)
            }
        }
        Text(
            text = colorText,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

val Color.hexValue: String
    get() = String.format(locale = Locale.ROOT, format = "#%08X", toArgb())

private fun getPalette(seed: Color?, colorScheme: ColorScheme): List<Pair<String, Color>> {
    val colorMap = mutableListOf<Pair<String, Color>>()
    ColorScheme::class.java.declaredFields.mapNotNullTo(colorMap) { field ->
        field.isAccessible = true
        val color = runCatching { Color((field.get(colorScheme) as Long).toULong()) }.getOrNull()
        val colorName = field.name.substringBefore('$')
        color?.let { colorName to color }
    }
    val primary = colorMap.firstOrNull { it.first.equals("primary", ignoreCase = true) }
    val secondary = colorMap.firstOrNull { it.first.equals("secondary", ignoreCase = true) }
    colorMap.remove(primary)
    colorMap.remove(secondary)
    colorMap.addAll(0, listOfNotNull(seed?.let { "Seed" to seed }, primary, secondary))
    return colorMap
}
