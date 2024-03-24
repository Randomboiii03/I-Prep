package com.example.i_prep.presentation.create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CViewModel : ViewModel() {

    private val _state: MutableStateFlow<CState> = MutableStateFlow(CState())
    val state = _state

    fun onReset(): Boolean {
        return state.value.questionType.isNotEmpty() || (state.value.fileName.isNotEmpty() && state.value.reference.isNotEmpty()) || state.value.language.isNotEmpty() || state.value.difficulty != "Easy"
    }

    fun onGenerate(): Boolean {
        return state.value.questionType.isNotEmpty() && (state.value.fileName.isNotEmpty() && state.value.reference.isNotEmpty()) && state.value.language.isNotEmpty()
    }

    fun onEvent(event: CEvent) {
        when (event) {
            is CEvent.SetForm -> _state.update { event.cState }
            CEvent.ResetForm -> _state.update {
                it.copy(
                    questionType = "",
                    fileName = "",
                    reference = "",
                    language = "",
                    difficulty = "Easy"
                )
            }
        }
    }
}

data class CState(
    val questionType: String = "",
    val fileName: String = "",
    val reference: String = "",
    val language: String = "",
    val difficulty: String = "Easy"
)

sealed interface CEvent {

    data class SetForm(val cState: CState) : CEvent
    object ResetForm : CEvent
}