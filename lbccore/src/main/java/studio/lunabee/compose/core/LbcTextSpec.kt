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
 * LbcTextSpec.kt
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 11/30/2022 - for the Lunabee Compose library.
 */
package studio.lunabee.compose.core

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString

@Stable
sealed class LbcTextSpec {

    abstract val annotated: AnnotatedString
        @ReadOnlyComposable @Composable
        get

    abstract val string: String
        @ReadOnlyComposable @Composable
        get

    @Composable
    @ReadOnlyComposable
    protected fun Array<out Any>.resolveArgs(): Array<out Any> {
        return Array(this.size) { idx ->
            val entry = this[idx]
            if (entry is LbcTextSpec) {
                entry.string // marked as compile error due to Java(?)
            } else {
                this[idx]
            }
        }
    }

    class Raw(private val value: String, private vararg val args: Any) : LbcTextSpec() {
        override val annotated: AnnotatedString
            @Composable
            @ReadOnlyComposable
            @Suppress("SpreadOperator")
            get() = if (args.isEmpty()) {
                AnnotatedString(value)
            } else {
                AnnotatedString(value.format(*args.resolveArgs()))
            }

        override val string: String
            @Composable
            @ReadOnlyComposable
            @Suppress("SpreadOperator")
            get() = if (args.isEmpty()) {
                value
            } else {
                value.format(*args.resolveArgs())
            }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Raw

            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }

        override fun toString(): String {
            return "value = $value"
        }
    }

    class Annotated(private val value: AnnotatedString) : LbcTextSpec() {
        override val annotated: AnnotatedString
            @Composable
            @ReadOnlyComposable
            get() = value

        override val string: String
            @Composable
            @ReadOnlyComposable
            get() = value.text

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Annotated

            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }

        override fun toString(): String {
            return "value = ${value.text}"
        }
    }

    class StringResource(
        @StringRes private val id: Int,
        private vararg val args: Any,
    ) : LbcTextSpec() {
        override val annotated: AnnotatedString
            @Composable
            @ReadOnlyComposable
            @Suppress("SpreadOperator")
            get() = AnnotatedString(stringResource(id, *args.resolveArgs()))

        override val string: String
            @Composable
            @ReadOnlyComposable
            @Suppress("SpreadOperator")
            get() = stringResource(id, *args.resolveArgs())

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StringResource

            if (id != other.id) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    class PluralsResource(
        @PluralsRes private val id: Int,
        private val count: Int,
        private vararg val args: Any,
    ) : LbcTextSpec() {

        override val annotated: AnnotatedString
            @Composable
            @ReadOnlyComposable
            @Suppress("SpreadOperator")
            get() = AnnotatedString(pluralStringResource(id, count, *args.resolveArgs()))

        override val string: String
            @Composable
            @ReadOnlyComposable
            @Suppress("SpreadOperator")
            get() = pluralStringResource(id, count, *args.resolveArgs())

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as PluralsResource

            if (id != other.id) return false
            if (count != other.count) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + count
            result = 31 * result + args.contentHashCode()
            return result
        }
    }
}
