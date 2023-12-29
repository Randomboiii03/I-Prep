package com.example.i_prep.presentation.history.composables.view

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.i_prep.common.emptyPTest
import com.example.i_prep.common.emptyTHistory
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.presentation.GlobalState
import com.randomboiii.i_prep.data.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class VVIewModel : ViewModel() {

    private val _state: MutableStateFlow<VState> = MutableStateFlow(VState())
    val state = _state

    fun onEvent(event: VEvent) {
        when (event) {
            VEvent.NextQuestion -> {
                if (state.value.pTest.itemSet > state.value.currentQIndex - 1) {
                    _state.update { it.copy(currentQIndex = state.value.currentQIndex + 1) }
                }
            }

            VEvent.PreviousQuestion ->
                if (state.value.currentQIndex > 0) {
                    _state.update { it.copy(currentQIndex = state.value.currentQIndex - 1) }
                }

            is VEvent.InitializeResult -> {
                _state.update {
                    it.copy(
                        pTest = event.pTest,
                        tHistory = event.tHistory,
                        isLoading = false,
                        currentQIndex = 0
                    )
                }
            }
        }
    }
}

data class VState(
    val pTest: PTest = emptyPTest,
    val tHistory: THistory = emptyTHistory,
    val currentQIndex: Int = 0,
    val isLoading: Boolean = true
)

sealed interface VEvent {
    data class InitializeResult(val pTest: PTest, val tHistory: THistory) : VEvent

    object PreviousQuestion : VEvent
    object NextQuestion : VEvent
}