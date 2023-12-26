package com.example.i_prep.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.i_prep.common.emptyPTest
import com.example.i_prep.common.emptyTHistory
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.domain.use_cases.DeleteHistory
import com.example.i_prep.domain.use_cases.DeleteTest
import com.example.i_prep.domain.use_cases.GetAllHistory
import com.example.i_prep.domain.use_cases.GetAllTest
import com.example.i_prep.domain.use_cases.GetLastHistory
import com.example.i_prep.domain.use_cases.InsertHistory
import com.example.i_prep.domain.use_cases.UpsertTest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel @Inject constructor(
    private val getAllTest: GetAllTest,
//    private val getTestById: GetTestById,
    private val upsertTest: UpsertTest,
    private val deleteTest: DeleteTest,
    private val getAllHistory: GetAllHistory,
//    private val getHistoryById: GetHistoryById,
//    private val getLastHistory: GetLastHistory,
    private val insertHistory: InsertHistory,
    private val deleteHistory: DeleteHistory
) : ViewModel() {
    private val _state: MutableStateFlow<GlobalState> = MutableStateFlow(GlobalState())
    val state = _state

    init {
        onEvent(GlobalEvent.GetAllTest)
        onEvent(GlobalEvent.GetAllHistory)
    }

    fun onEvent(event: GlobalEvent) {
        when (event) {
            is GlobalEvent.DeleteHistory -> {
                viewModelScope.launch {
                    deleteHistory(event.tHistory)

                    _state.update { it.copy(tHistory = emptyTHistory) }
                }
            }

            is GlobalEvent.DeleteTest -> {
                viewModelScope.launch {
                    deleteTest(event.pTest)

                    _state.update { it.copy(pTest = emptyPTest) }
                }
            }

            GlobalEvent.GetAllHistory -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    _state.update {
                        it.copy(
                            tHistoryList = getAllHistory().first(),
                            isLoading = false
                        )
                    }
                }
            }

            GlobalEvent.GetAllTest -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    _state.update {
                        it.copy(pTestList = getAllTest().first(), isLoading = false)
                    }
                }
            }

            is GlobalEvent.GetHistory -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    _state.update {
                        it.copy(tHistory = event.tHistory, isLoading = false)
                    }
                }
            }

            is GlobalEvent.GetTest -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    _state.update {
                        it.copy(pTest = event.pTest, showBottomNav = false, isLoading = false)
                    }
                }
            }

            is GlobalEvent.InsertHistory -> {
                viewModelScope.launch {
                    insertHistory(event.tHistory)
                }
            }

            is GlobalEvent.UpsertTest -> {
                viewModelScope.launch {

                    _state.update {
                        it.copy(
                            pTest = event.pTest,
                            pTestList = state.value.pTestList.map { pTest ->
                                if (pTest.testId == event.pTest.testId) {
                                    event.pTest
                                } else pTest
                            })
                    }
                    upsertTest(event.pTest)
                }
            }

            is GlobalEvent.SearchTest -> {
                _state.update { it.copy(isLoading = true) }
                if (event.query.isBlank()) {
                    _state.update {
                        it.copy(
                            pTestListFiltered = state.value.pTestList,
                            isLoading = false
                        )
                    }
                } else {
                    val filteredPTest = state.value.pTestList.filter {
                        it.doesMatchQuery(event.query)
                    }
                    _state.update { it.copy(pTestListFiltered = filteredPTest, isLoading = false) }
                }
            }

            GlobalEvent.ShowBottomNav -> _state.update { it.copy(showBottomNav = true) }
        }
    }
}

data class GlobalState(
    val pTestList: List<PTest> = emptyList(),
    val pTestListFiltered: List<PTest> = emptyList(),
    val pTest: PTest = emptyPTest,
    val tHistoryList: List<THistory> = emptyList(),
    val tHistory: THistory = emptyTHistory,
    val showBottomNav: Boolean = true,
    val isLoading: Boolean = false
)

sealed interface GlobalEvent {
    object GetAllTest : GlobalEvent
    data class UpsertTest(val pTest: PTest) : GlobalEvent
    data class DeleteTest(val pTest: PTest) : GlobalEvent

    object GetAllHistory : GlobalEvent
    data class InsertHistory(val tHistory: THistory) : GlobalEvent
    data class DeleteHistory(val tHistory: THistory) : GlobalEvent

    data class SearchTest(val query: String) : GlobalEvent
    data class GetTest(val pTest: PTest) : GlobalEvent
    object ShowBottomNav : GlobalEvent

    data class GetHistory(val tHistory: THistory) : GlobalEvent
}