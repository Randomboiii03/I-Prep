package com.example.i_prep.presentation.create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CViewModel : ViewModel() {

    private val _state: MutableStateFlow<CState> = MutableStateFlow(CState())
    val state = _state

    fun onReset(): Boolean {
        return state.value.questionType.isNotEmpty() || (state.value.fileName.isNotEmpty() && state.value.filePath.isNotEmpty()) || state.value.language.isNotEmpty() || state.value.difficulty != "Easy"
    }

    fun onGenerate(): Boolean {
        return state.value.questionType.isNotEmpty() && (state.value.fileName.isNotEmpty() && state.value.filePath.isNotEmpty()) && state.value.language.isNotEmpty()
    }

    fun onEvent(event: CEvent) {
        when (event) {
            CEvent.Reset -> _state.update {
                it.copy(
                    questionType = "",
                    fileName = "",
                    filePath = "",
                    language = "",
                    difficulty = "Easy"
                )
            }

            is CEvent.SetDifficulty -> _state.update { it.copy(difficulty = event.difficulty) }
            is CEvent.SetLanguage -> _state.update { it.copy(language = event.language) }
            is CEvent.SetQuestionType -> _state.update { it.copy(questionType = event.questionType) }
            is CEvent.UploadFile -> _state.update {
                it.copy(fileName = event.fileName, filePath = event.filePath)
            }

            is CEvent.Generate -> _state.update { it.copy(isGenerate = event.isGenerate) }
        }
    }
}

data class CState(
    val questionType: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val language: String = "",
    val difficulty: String = "Easy",
    val isGenerate: Boolean = false
)

sealed interface CEvent {
    object Reset : CEvent

    data class SetQuestionType(val questionType: String) : CEvent
    data class UploadFile(val fileName: String, val filePath: String) : CEvent
    data class SetLanguage(val language: String) : CEvent
    data class SetDifficulty(val difficulty: String) : CEvent
    data class Generate(val isGenerate: Boolean) : CEvent
}