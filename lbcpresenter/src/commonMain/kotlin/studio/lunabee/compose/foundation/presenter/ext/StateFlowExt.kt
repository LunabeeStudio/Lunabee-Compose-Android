package studio.lunabee.compose.foundation.presenter.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow

@Composable
internal expect fun <T> StateFlow<T>.collectAsStateWithLifecycleCompat(): State<T>
