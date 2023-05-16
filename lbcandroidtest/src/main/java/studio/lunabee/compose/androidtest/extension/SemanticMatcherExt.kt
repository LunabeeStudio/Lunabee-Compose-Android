@file:Suppress("UNUSED")

package studio.lunabee.compose.androidtest.extension

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.waitUntilNodeCount
import studio.lunabee.compose.androidtest.LbcAndroidTestConstants
import studio.lunabee.compose.androidtest.rule.LbcPrintRule
import kotlin.time.Duration

/**
 * Wait for the [SemanticsNodeInteraction] corresponding to the provided [SemanticsMatcher] and return it condition is filled
 * (i.e element does not exist anymore). No need to call [SemanticsNodeInteraction.assertDoesNotExist] after as the condition is
 * already check by the waitUntil.
 *
 * Example:
 * ```
 * hasTestTag("MyTag")
 *     .waitUntilDoesNotExist(this, false, 1.seconds)
 * ```
 */
@OptIn(ExperimentalTestApi::class)
fun SemanticsMatcher.waitUntilDoesNotExist(
    composeUiTest: ComposeUiTest,
    useUnmergedTree: Boolean = false,
    timeoutMillis: Duration = LbcAndroidTestConstants.WaitNodeTimeout,
): SemanticsNodeInteraction {
    // Method copy from ComposeUiTest.waitUntilExactlyOneExists to add useUnmergedTree.
    // Waiting for answer: https://issuetracker.google.com/issues/268432145
    return waitUntilNodeCount(composeUiTest, 0, useUnmergedTree, timeoutMillis).filterToOne(this)
}

/**
 * Wait for the [SemanticsNodeInteraction] corresponding to the provided [SemanticsMatcher] and return it condition is filled
 * (i.e element exists).
 * ⚠️ It doesn't mean that the element is displayed. You should check the visibility with
 * [androidx.compose.ui.test.assertIsDisplayed].
 *
 * This method is a shortcut to [waitUntilNodeCount].
 *
 * Example:
 * ```
 * hasTestTag("MyTag")
 *     .waitUntilExactlyOneExists(this, false, 1.seconds)
 *     .assertIdDisplayed() // additional check
 * ```
 */
@OptIn(ExperimentalTestApi::class)
fun SemanticsMatcher.waitUntilExactlyOneExists(
    composeUiTest: ComposeUiTest,
    useUnmergedTree: Boolean = false,
    timeoutMillis: Duration = LbcAndroidTestConstants.WaitNodeTimeout,
): SemanticsNodeInteraction {
    // Method copy from ComposeUiTest.waitUntilExactlyOneExists to add useUnmergedTree.
    // Waiting for answer: https://issuetracker.google.com/issues/268432145
    return waitUntilNodeCount(composeUiTest, 1, useUnmergedTree, timeoutMillis).filterToOne(this)
}

/**
 * Wait for the [SemanticsNodeInteraction] corresponding to the provided [SemanticsMatcher] and return it condition is filled
 * (i.e element exists for the desired [count]).
 * It will return a list of node matching your [SemanticsMatcher].
 *
 * ⚠️ It doesn't mean that all elements are displayed. You should check the visibility with
 * [androidx.compose.ui.test.assertIsDisplayed].
 *
 * Example:
 * ```
 * hasTestTag("MyTag")
 *     .waitUntilNodeCount(this, 10, false, 1.seconds)
 *     .assertIdDisplayed() // additional check
 * ```
 */
@OptIn(ExperimentalTestApi::class)
fun SemanticsMatcher.waitUntilNodeCount(
    composeUiTest: ComposeUiTest,
    count: Int,
    useUnmergedTree: Boolean = false,
    timeoutMillis: Duration = LbcAndroidTestConstants.WaitNodeTimeout,
): SemanticsNodeInteractionCollection {
    // Method copy from ComposeUiTest.waitUntilExactlyOneExists to add useUnmergedTree.
    // Waiting for answer: https://issuetracker.google.com/issues/268432145
    composeUiTest.waitUntil(timeoutMillis.inWholeMilliseconds) {
        composeUiTest.onAllNodes(this, useUnmergedTree).fetchSemanticsNodes().size == count
    }
    return composeUiTest.onAllNodes(this, useUnmergedTree)
}

/**
 * Wait for the [SemanticsNodeInteraction] corresponding to the provided [SemanticsMatcher] and return it condition is filled
 * (i.e element exists multiple times but no need to wait for more.).
 * It will return a list of node matching your [SemanticsMatcher] available when the condition is filled.
 *
 * ⚠️ It doesn't mean that all elements are displayed. You should check the visibility with
 * [androidx.compose.ui.test.assertIsDisplayed].
 *
 * Example:
 * ```
 * hasTestTag("MyTag")
 *     .waitUntilAtLeastOneExists(this, false, 1_000L)
 *     .assertIdDisplayed() // additional check
 * ```
 */
@OptIn(ExperimentalTestApi::class)
fun SemanticsMatcher.waitUntilAtLeastOneExists(
    composeUiTest: ComposeUiTest,
    useUnmergedTree: Boolean = false,
    timeoutMillis: Duration = LbcAndroidTestConstants.WaitNodeTimeout,
): SemanticsNodeInteractionCollection {
    // Method copy from ComposeUiTest.waitUntilExactlyOneExists to add useUnmergedTree.
    // Waiting for answer: https://issuetracker.google.com/issues/268432145
    composeUiTest.waitUntil(timeoutMillis.inWholeMilliseconds) {
        composeUiTest.onAllNodes(this, useUnmergedTree).fetchSemanticsNodes().isNotEmpty()
    }
    return composeUiTest.onAllNodes(this, useUnmergedTree)
}

/**
 * Wait for the [SemanticsNodeInteraction] corresponding to the provided [SemanticsMatcher] and return it condition is filled
 * (i.e element exists).
 * Before returning the element, a screenshot of the root node is taken. If the condition is not filled, a screenshot is also
 * taken with the suffix "_TIMEOUT" to see what went wrong.
 *
 * ⚠️ It doesn't mean that all elements are displayed. You should check the visibility with
 * [androidx.compose.ui.test.assertIsDisplayed].
 *
 * Example:
 * ```
 * hasTestTag("MyTag")
 *     .waitAndPrintRootToCacheDir(composeUiTest, printRule, "_suffix", false, 1.seconds)
 *     .performClick() // additional check
 * ```
 */
@OptIn(ExperimentalTestApi::class)
fun SemanticsMatcher.waitAndPrintRootToCacheDir(
    composeUiTest: ComposeUiTest,
    printRule: LbcPrintRule,
    suffix: String = "",
    useUnmergedTree: Boolean = false,
    timeoutMillis: Duration = LbcAndroidTestConstants.WaitNodeTimeout,
): SemanticsNodeInteraction {
    return try {
        waitUntilExactlyOneExists(composeUiTest, useUnmergedTree, timeoutMillis)
        composeUiTest.onRoot().printToCacheDir(printRule, suffix)
        composeUiTest.onNode(this)
    } catch (e: ComposeTimeoutException) {
        composeUiTest.onRoot(useUnmergedTree = useUnmergedTree)
            .printToCacheDir(printRule, "${suffix}_TIMEOUT")
        throw e
    }
}

/**
 * Wait for the [SemanticsNodeInteraction] corresponding to the provided [SemanticsMatcher] and return it condition is filled
 * (i.e element exists).
 * Before returning the element, a screenshot of the whole screen is taken. If the condition is not filled, a screenshot is also
 * taken with the suffix "_TIMEOUT" to see what went wrong.
 *
 * Should be use when the UI contains many root node, like when a dialog is shown. In other cases, [waitAndPrintRootToCacheDir] must be
 * preferred.
 *
 * ⚠️ It doesn't mean that all elements are displayed. You should check the visibility with
 * [androidx.compose.ui.test.assertIsDisplayed].
 *
 * Example of waiting for a screen:
 * ```
 * hasTestTag("MyTag")
 *     .waitAndPrintWholeScreenToCacheDir(composeUiTest, printRule, "_suffix", false, 1.seconds)
 * ```
 */
@OptIn(ExperimentalTestApi::class)
fun SemanticsMatcher.waitAndPrintWholeScreenToCacheDir(
    composeUiTest: ComposeUiTest,
    printRule: LbcPrintRule,
    suffix: String = "",
    useUnmergedTree: Boolean = false,
    timeoutMillis: Duration = LbcAndroidTestConstants.WaitNodeTimeout,
): SemanticsNodeInteraction {
    return try {
        waitUntilAtLeastOneExists(composeUiTest, useUnmergedTree, timeoutMillis)
        printRule.printWholeScreen(suffix = suffix)
        composeUiTest.onAllNodes(this).filterToOne(this)
    } catch (e: ComposeTimeoutException) {
        printRule.printWholeScreen(suffix = "${suffix}_TIMEOUT")
        throw e
    }
}

/**
 * Log after a semantic action. Sometimes there is exception during test that does not point to our code.
 * This method can be used as debugging tool for flaky tests 🤡.
 * Example:
 * ```
 * hasText(getString(R.string.string))
 *      .waitAndPrintRootToCacheDir(this, printRule, "_suffix")
 *      .consoleLog("printToCacheDir")
 *      .performScrollTo()
 *      .consoleLog("scrollTo")
 *      .performClick()
 * ```
 * Will return a default output like this:
 * ConsoleTestLog - On node with TestTag MyTestTag - isRoot? false - printToCacheDir
 * ConsoleTestLog - On node with TestTag MyTestTag - isRoot? false - scrollTo
 */
fun SemanticsNodeInteraction.consoleLog(
    message: String,
    tag: String = "ConsoleTestLog",
): SemanticsNodeInteraction {
    val semanticsNode = fetchSemanticsNode()
    val config = semanticsNode.config
    val (type, value) = when {
        config.getOrNull(SemanticsProperties.Text) != null -> SemanticsProperties.Text.name to config[SemanticsProperties.Text]
        config.getOrNull(SemanticsProperties.ContentDescription) != null ->
            SemanticsProperties.ContentDescription.name to config[SemanticsProperties.ContentDescription]

        config.getOrNull(SemanticsProperties.TestTag) != null ->
            SemanticsProperties.TestTag.name to config[SemanticsProperties.TestTag]

        else -> throw IllegalArgumentException("Not handled")
    }
    println("$tag - On node with $type $value - isRoot? ${semanticsNode.isRoot} - $message")
    return this
}
