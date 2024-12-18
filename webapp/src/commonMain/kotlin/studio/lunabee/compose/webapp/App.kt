package studio.lunabee.compose.webapp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.ExperimentalResourceApi
import studio.lunabee.compose.core.LbcImageSpec
import studio.lunabee.compose.core.LbcTextSpec
import studio.lunabee.compose.image.LbcImage
import studio.lunabee.compose.webapp.generated.resources.Res
import studio.lunabee.compose.webapp.generated.resources.allStringResources
import studio.lunabee.compose.webapp.generated.resources.fallback
import studio.lunabee.compose.webapp.generated.resources.ic_share
import studio.lunabee.compose.webapp.generated.resources.test
import studio.lunabee.compose.webapp.generated.resources.test_args
import studio.lunabee.compose.webapp.generated.resources.test_args_plural
import studio.lunabee.compose.webapp.generated.resources.test_other_args

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    Column {
        Text(
            "Hello: ${LbcTextSpec.StringResource(Res.string.test).string}",
        )
        Text(
            "Test with string params: ${
                LbcTextSpec.StringResource(
                    Res.string.test_other_args,
                    "foo",
                    123,
                ).string
            }",
        )
        Text(
            "Test with recursive string params: ${
                LbcTextSpec.StringResource(
                    Res.string.test_args,
                    LbcTextSpec.StringResource(Res.string.test),
                    123,
                ).string
            }",
        )
        Text(
            "Test plural: ${
                LbcTextSpec.PluralsResource(
                    Res.plurals.test_args_plural,
                    1,
                    1,
                ).string
            } && ${
                LbcTextSpec.PluralsResource(
                    Res.plurals.test_args_plural,
                    2,
                    2,
                ).string
            }",
        )
        Text(
            "Test by name: ${
                LbcTextSpec.StringByNameResource(
                    key = "test",
                    fallbackResource = Res.string.fallback,
                    allStringResources = Res.allStringResources,
                ).string
            } && ${
                LbcTextSpec.StringByNameResource(
                    key = "test_args",
                    allStringResources = Res.allStringResources,
                    fallbackResource = Res.string.fallback,
                    "foo",
                    123,
                ).string
            } && ${
                LbcTextSpec.StringByNameResource(
                    key = "does_not_exist",
                    allStringResources = Res.allStringResources,
                    fallbackResource = Res.string.fallback,
                    "foo",
                    123,
                ).string
            }",
        )

        LbcImage(
            imageSpec = LbcImageSpec.Icon(
                resource = Res.drawable.ic_share,
                tint = { Color.Red },
            ),
        )
        LbcImage(
            imageSpec = LbcImageSpec.Url(
                url = "https://cdn.getmidnight.com/26ffcef53c44522efbfe7fef964a4058/2023/02/untitled-1-.png",
            ),
        )
    }
}
