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
 * LbcTextSpec.kt
 * Lunabee Compose
 *
 * Created by Lunabee Studio / Date - 11/20/2024 - for the Lunabee Compose library.
 */

package studio.lunabee.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import studio.lunabee.compose.core.ext.format
import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode
import kotlin.collections.isEmpty
import kotlin.js.JsName

@Stable
sealed class LbcTextSpec {
    abstract val annotated: AnnotatedString
        @Composable
        get

    abstract val string: String
        @Composable
        get

    @JsName("stringBlocking")
    abstract suspend fun string(): String

    @Composable
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

    protected suspend fun Array<out Any>.resolveArgsSuspend(): Array<out Any> {
        return Array(this.size) { idx ->
            val entry = this[idx]
            if (entry is LbcTextSpec) {
                entry.string() // marked as compile error due to Java(?)
            } else {
                this[idx]
            }
        }
    }

    class Raw(
        private val value: String,
        private vararg val args: Any,
    ) : LbcTextSpec() {
        override val annotated: AnnotatedString
            @Composable
            get() = AnnotatedString(string)
        override val string: String
            @Composable
            @Suppress("SpreadOperator")
            get() = if (args.isEmpty()) {
                value
            } else {
                value.format(*args.resolveArgs())
            }

        override suspend fun string(): String =
            if (args.isEmpty()) {
                value
            } else {
                @Suppress("SpreadOperator")
                value.format(*args.resolveArgsSuspend())
            }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Raw

            return value == other.value
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }

        override fun toString(): String {
            return "value = $value"
        }
    }

    class Annotated(
        private val value: AnnotatedString,
    ) : LbcTextSpec() {
        override val annotated: AnnotatedString
            @Composable
            get() = value
        override val string: String
            @Composable
            get() = value.text

        override suspend fun string(): String = value.text

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Annotated

            return value == other.value
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }

        override fun toString(): String {
            return "value = ${value.text}"
        }
    }

    class StringResource(
        val resource: org.jetbrains.compose.resources.StringResource,
        vararg val args: Any,
    ) : LbcTextSpec() {
        override val annotated: AnnotatedString
            @Composable
            get() = AnnotatedString(string)
        override val string: String
            /*
            We cannot use the stringResource function with args because the param format is limited with it
            (cannot use %s or %d format)
             */
            @Composable
            @Suppress("SpreadOperator")
            get() = stringResource(resource).format(*args.resolveArgs())

        override suspend fun string(): String =
            @Suppress("SpreadOperator")
            getString(resource, *args.resolveArgsSuspend())

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as StringResource

            if (resource == other.resource) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resource.hashCode()
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    class PluralsResource(
        val resource: PluralStringResource,
        val count: Int,
        vararg val args: Any,
    ) : LbcTextSpec() {
        override val annotated: AnnotatedString
            @Composable
            get() = AnnotatedString(string)
        override val string: String
            /*
            We cannot use the stringResource function with args because the param format is limited with it
            (cannot use %s or %d format)
             */
            @Composable
            @Suppress("SpreadOperator")
            get() = pluralStringResource(resource, count).format(*args.resolveArgs())

        override suspend fun string(): String =
            @Suppress("SpreadOperator")
            getPluralString(resource, count, *args.resolveArgsSuspend())

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as PluralsResource

            if (resource == other.resource) return false
            if (count == other.count) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = count
            result = 31 * result + result.hashCode()
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    class StringByNameResource(
        private val key: String,
        private val allStringResources: Map<String, org.jetbrains.compose.resources.StringResource>,
        private val fallbackResource: org.jetbrains.compose.resources.StringResource,
        private vararg val args: Any,
    ) : LbcTextSpec() {
        override val annotated: AnnotatedString
            @Composable
            get() = AnnotatedString(string)

        @OptIn(ExperimentalResourceApi::class)
        override val string: String
            @Composable
            @Suppress("SpreadOperator")
            get() {
                /*
                We cannot use the stringResource function with args because the param format is limited with it
                (cannot use %s or %d format)
                 */
                val resource: org.jetbrains.compose.resources.StringResource =
                    allStringResources[key] ?: fallbackResource

                return stringResource(resource).format(*args.resolveArgs())
            }

        override suspend fun string(): String =
            @Suppress("SpreadOperator")
            getString(
                resource = allStringResources[key] ?: fallbackResource,
                *args.resolveArgsSuspend(),
            )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as StringByNameResource

            if (key != other.key) return false
            if (fallbackResource != other.fallbackResource) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = key.hashCode()
            result = 31 * result + fallbackResource.hashCode()
            result = 31 * result + args.contentHashCode()
            return result
        }
    }
}
