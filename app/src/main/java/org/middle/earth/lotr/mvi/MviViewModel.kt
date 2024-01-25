package org.middle.earth.lotr.mvi

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.LazyThreadSafetyMode.NONE

private const val TAG = "MviViewModel"

abstract class MviViewModel<in UiEvent : MoleculeUiEvent, out UiState : MoleculeUiState> : ViewModel() {
    private val molecularScope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val uiEventFlow = MutableSharedFlow<UiEvent>(replay = 0, extraBufferCapacity = 11, onBufferOverflow = BufferOverflow.SUSPEND)

    val uiStateFlow: StateFlow<UiState> by lazy(NONE) { molecularScope.launchMolecule(mode = RecompositionMode.ContextClock) { state(uiEventFlow) } }

    fun reduce(uiEvent: UiEvent) {
        val emittedSuccessfully = uiEventFlow.tryEmit(uiEvent)
        if (emittedSuccessfully) return

        val message = "Failed to emit even $uiEvent"
        val exception = RuntimeException(message)
        Log.e(TAG, "reduce: $message", exception)
        Firebase.crashlytics.recordException(exception)
    }

    @Composable
    protected abstract fun state(events: Flow<UiEvent>): UiState
}
