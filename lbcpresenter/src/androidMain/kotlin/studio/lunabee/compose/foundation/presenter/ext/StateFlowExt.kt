package studio.lunabee.compose.foundation.presenter.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
internal actual fun <T> StateFlow<T>.collectAsStateWithLifecycleCompat(): State<T> =
    this.collectAsStateWithLifecycle()
