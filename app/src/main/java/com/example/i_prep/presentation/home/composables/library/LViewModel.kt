package com.example.i_prep.presentation.home.composables.library

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LViewModel : ViewModel() {
    private val _state: MutableStateFlow<LState> = MutableStateFlow(LState())
    val state = _state

    fun onEvent(event: LEvent) {
        when (event) {
            LEvent.Filter -> _state.update { it.copy(showFilter = !state.value.showFilter, isSearch = false) }
            LEvent.Search -> {
                _state.update { it.copy(isSearch = !state.value.isSearch, searchText = if (!state.value.isSearch) "" else state.value.searchText) }
            }
            is LEvent.onSearch -> {
                _state.update { it.copy(searchText = event.searchText) }
            }
        }
    }
}

data class LState(
    val isSearch: Boolean = false,
    val showFilter: Boolean = false,
    val searchText: String = ""
)

sealed interface LEvent {
    object Search : LEvent
    data class onSearch(val searchText: String) : LEvent

    object Filter : LEvent
}