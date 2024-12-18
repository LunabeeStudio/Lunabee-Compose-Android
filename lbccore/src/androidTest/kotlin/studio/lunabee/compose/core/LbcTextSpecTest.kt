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
 * LbcTextSpecTest.kt
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 12/01/2022 - for the Lunabee Compose library.
 */
package studio.lunabee.compose.core

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.AnnotatedString
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class LbcTextSpecTest {
    @get:Rule
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity> =
        createAndroidComposeRule()

    private val context: Context
        get() = composeTestRule.activity.baseContext

    @Test
    fun raw_test() {
        val expected = "test"
        val textSpec = LbcTextSpec.Raw(value = expected)
        assertEquals(expected, textSpec.string(context))
        assertEquals(AnnotatedString(expected), textSpec.annotated(context))
        composeTestRule.setContent {
            assertEquals(expected, textSpec.string)
            assertEquals(AnnotatedString(expected), textSpec.annotated)
        }
    }

    @Test
    fun raw_args_test() {
        val param = "param"
        val test = "test %s"
        val expected = test.format(param)
        val textSpec = LbcTextSpec.Raw(value = test, param)
        assertEquals(expected, textSpec.string(context))
        assertEquals(AnnotatedString(expected), textSpec.annotated(context))
        composeTestRule.setContent {
            assertEquals(expected, textSpec.string)
            assertEquals(AnnotatedString(expected), textSpec.annotated)
        }
    }

    @Test
    fun annotated_test() {
        val expected = AnnotatedString("test")
        val textSpec = LbcTextSpec.Annotated(value = expected)
        assertEquals(expected, textSpec.annotated(context))
        assertEquals(expected.text, textSpec.string(context))
        composeTestRule.setContent {
            assertEquals(expected, textSpec.annotated)
            assertEquals(expected.text, textSpec.string)
        }
    }

    @Test
    fun stringRes_test() {
        val expectedTest = composeTestRule.activity.getString(studio.lunabee.compose.core.test.R.string.test)
        val actualTest = LbcTextSpec.StringResource(id = studio.lunabee.compose.core.test.R.string.test)

        val stringParam = "foo"
        val intParam = 123
        val expectedTestArgs = composeTestRule
            .activity
            .getString(studio.lunabee.compose.core.test.R.string.test_args, stringParam, intParam)
        val actualTestArgs =
            LbcTextSpec.StringResource(id = studio.lunabee.compose.core.test.R.string.test_args, stringParam, intParam)

        assertEquals(expectedTest, actualTest.string(context))
        assertEquals(expectedTestArgs, actualTestArgs.string(context))
        assertEquals(AnnotatedString(expectedTest), actualTest.annotated(context))
        assertEquals(AnnotatedString(expectedTestArgs), actualTestArgs.annotated(context))
        composeTestRule.setContent {
            assertEquals(expectedTest, actualTest.string)
            assertEquals(expectedTestArgs, actualTestArgs.string)

            assertEquals(AnnotatedString(expectedTest), actualTest.annotated)
            assertEquals(AnnotatedString(expectedTestArgs), actualTestArgs.annotated)
        }
    }

    @Test
    fun stringRes_recursive_test() {
        val intParam = 123
        val stringParam = composeTestRule.activity.getString(studio.lunabee.compose.core.test.R.string.test)
        val stringParamTextSpec = LbcTextSpec.StringResource(id = studio.lunabee.compose.core.test.R.string.test)

        val expectedTestArgs = composeTestRule.activity.getString(
            studio.lunabee.compose.core.test.R.string.test_args,
            stringParam,
            intParam,
        )
        val actualTestArgs = LbcTextSpec.StringResource(
            studio.lunabee.compose.core.test.R.string.test_args,
            stringParamTextSpec,
            intParam,
        )

        assertEquals(expectedTestArgs, actualTestArgs.string(context))
        assertEquals(AnnotatedString(expectedTestArgs), actualTestArgs.annotated(context))
        composeTestRule.setContent {
            assertEquals(expectedTestArgs, actualTestArgs.string)
            assertEquals(AnnotatedString(expectedTestArgs), actualTestArgs.annotated)
        }
    }

    @Test
    fun pluralsRes_test() {
        val expectedTestOne = composeTestRule
            .activity
            .resources
            .getQuantityString(studio.lunabee.compose.core.test.R.plurals.test_args_plural, 1, 1)
        val actualTestOne =
            LbcTextSpec.PluralsResource(studio.lunabee.compose.core.test.R.plurals.test_args_plural, 1, 1)

        val expectedTestMany = composeTestRule
            .activity
            .resources
            .getQuantityString(studio.lunabee.compose.core.test.R.plurals.test_args_plural, 2, 2)
        val actualTestMany =
            LbcTextSpec.PluralsResource(studio.lunabee.compose.core.test.R.plurals.test_args_plural, 2, 2)

        assertEquals(expectedTestOne, actualTestOne.string(context))
        assertEquals(expectedTestMany, actualTestMany.string(context))
        assertEquals(AnnotatedString(expectedTestOne), actualTestOne.annotated(context))
        assertEquals(AnnotatedString(expectedTestMany), actualTestMany.annotated(context))
        composeTestRule.setContent {
            assertEquals(expectedTestOne, actualTestOne.string)
            assertEquals(expectedTestMany, actualTestMany.string)

            assertEquals(AnnotatedString(expectedTestOne), actualTestOne.annotated)
            assertEquals(AnnotatedString(expectedTestMany), actualTestMany.annotated)
        }
    }

    @Test
    fun stringByName_test() {
        val expectedTest = composeTestRule.activity.getString(studio.lunabee.compose.core.test.R.string.test)
        val actualTest = LbcTextSpec.StringByNameResource(name = "test", fallbackId = -1)
        val actualFallbackTest = LbcTextSpec.StringByNameResource(
            name = "does_not_exist",
            fallbackId = studio.lunabee.compose.core.test.R.string.test,
        )

        val stringParam = "foo"
        val intParam = 123
        val expectedTestArgs = composeTestRule
            .activity
            .getString(studio.lunabee.compose.core.test.R.string.test_args, stringParam, intParam)
        val actualTestArgs = LbcTextSpec.StringByNameResource(
            name = "test_args",
            fallbackId = -1,
            stringParam,
            intParam,
        )
        val actualTestFallbackArgs = LbcTextSpec.StringByNameResource(
            name = "does_not_exist",
            fallbackId = studio.lunabee.compose.core.test.R.string.test_args,
            stringParam,
            intParam,
        )

        assertEquals(expectedTest, actualTest.string(context))
        assertEquals(expectedTest, actualFallbackTest.string(context))
        assertEquals(expectedTestArgs, actualTestArgs.string(context))
        assertEquals(expectedTestArgs, actualTestFallbackArgs.string(context))
        assertEquals(AnnotatedString(expectedTest), actualTest.annotated(context))
        assertEquals(AnnotatedString(expectedTest), actualFallbackTest.annotated(context))
        assertEquals(AnnotatedString(expectedTestArgs), actualTestArgs.annotated(context))
        assertEquals(AnnotatedString(expectedTestArgs), actualTestFallbackArgs.annotated(context))
        composeTestRule.setContent {
            assertEquals(expectedTest, actualTest.string)
            assertEquals(expectedTest, actualFallbackTest.string)
            assertEquals(expectedTestArgs, actualTestArgs.string)
            assertEquals(expectedTestArgs, actualTestFallbackArgs.string)

            assertEquals(AnnotatedString(expectedTest), actualTest.annotated)
            assertEquals(AnnotatedString(expectedTest), actualFallbackTest.annotated)
            assertEquals(AnnotatedString(expectedTestArgs), actualTestArgs.annotated)
            assertEquals(AnnotatedString(expectedTestArgs), actualTestFallbackArgs.annotated)
        }
    }
}
