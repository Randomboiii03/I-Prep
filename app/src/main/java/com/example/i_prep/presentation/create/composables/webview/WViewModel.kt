package com.example.i_prep.presentation.create.composables.webview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class WViewModel : ViewModel() {
    private val _state: MutableStateFlow<WState> = MutableStateFlow(WState())
    val state = _state

    fun onEvent(event: WEvent) {
        when(event) {
            is WEvent.SetLoading -> _state.update { it.copy(isLoading = event.isLoading) }
            is WEvent.SetStatus -> _state.update { it.copy(status = event.status) }
            is WEvent.ShowDialog -> _state.update { it.copy(showDialog = event.showDialog) }
        }
    }
}

data class WState(
    val status: Boolean = false,
    val isLoading: Boolean = true,
    val showDialog: Boolean = false
)

sealed interface WEvent {
    data class SetStatus(val status: Boolean): WEvent
    data class SetLoading(val isLoading: Boolean): WEvent
    data class ShowDialog(val showDialog: Boolean): WEvent
}