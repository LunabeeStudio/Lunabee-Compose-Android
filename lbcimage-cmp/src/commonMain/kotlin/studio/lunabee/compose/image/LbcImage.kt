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
 * LbcImage.kt
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 11/19/2024 - for the Lunabee Compose library.
 */

package studio.lunabee.compose.image

import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import org.jetbrains.compose.resources.painterResource
import studio.lunabee.compose.core.LbcImageSpec
import studio.lunabee.compose.core.LbcTextSpec

@Composable
fun LbcImage(
    imageSpec: LbcImageSpec,
    modifier: Modifier = Modifier,
    contentDescription: LbcTextSpec? = null,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    colorFilter: ColorFilter? = null,
    errorPainter: Painter? = null,
) {
    when (imageSpec) {
        is LbcImageSpec.Bitmap -> {
            Image(
                bitmap = imageSpec.bitmap,
                contentDescription = contentDescription?.string,
                modifier = modifier,
                contentScale = contentScale,
                alignment = alignment,
                colorFilter = colorFilter,
            )
        }

        is LbcImageSpec.ImageDrawable -> {
            Image(
                painter = painterResource(imageSpec.resource),
                contentDescription = contentDescription?.string,
                modifier = modifier,
                contentScale = contentScale,
                alignment = alignment,
                colorFilter = colorFilter,
            )
        }

        is LbcImageSpec.Icon -> {
            val tint = imageSpec.tint.invoke().takeIf { it != Color.Unspecified } ?: LocalContentColor.current
            Icon(
                painter = painterResource(imageSpec.resource),
                contentDescription = contentDescription?.string,
                modifier = modifier,
                tint = tint,
            )
        }

        is LbcImageSpec.KtImageVector -> {
            val tint = imageSpec.tint.invoke().takeIf { it != Color.Unspecified } ?: LocalContentColor.current
            Icon(
                imageVector = imageSpec.icon,
                contentDescription = contentDescription?.string,
                modifier = modifier,
                tint = tint,
            )
        }

        is LbcImageSpec.Url -> {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageSpec.url)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = contentDescription?.string,
                modifier = modifier,
                alignment = alignment,
                contentScale = contentScale,
                error = errorPainter,
                onError = onState,
                onLoading = onState,
                onSuccess = onState,
                colorFilter = colorFilter,
            )
        }

        is LbcImageSpec.ByteArray ->
            AsyncImage(
                model = imageSpec.byteArray,
                contentDescription = contentDescription?.string,
                modifier = modifier,
                alignment = alignment,
                contentScale = contentScale,
                error = errorPainter,
                onError = onState,
                onLoading = onState,
                onSuccess = onState,
                colorFilter = colorFilter,
            )

        is LbcImageSpec.Uri ->
            AsyncImage(
                model = imageSpec.uri,
                contentDescription = contentDescription?.string,
                modifier = modifier,
                alignment = alignment,
                contentScale = contentScale,
                error = errorPainter,
                onError = onState,
                onLoading = onState,
                onSuccess = onState,
                colorFilter = colorFilter,
            )
    }
}
