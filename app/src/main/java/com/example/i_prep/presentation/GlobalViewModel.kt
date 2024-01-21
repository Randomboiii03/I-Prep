package com.example.i_prep.presentation

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.i_prep.common.compareToVersion
import com.example.i_prep.common.emptyPTest
import com.example.i_prep.common.emptyTHistory
import com.example.i_prep.common.latestVersion
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.domain.app_updater.AppUpdater
import com.example.i_prep.domain.app_updater.downloader.IPrepDownloader
import com.example.i_prep.domain.use_cases.DeleteHistory
import com.example.i_prep.domain.use_cases.DeleteTest
import com.example.i_prep.domain.use_cases.GetAllHistory
import com.example.i_prep.domain.use_cases.GetAllTest
import com.example.i_prep.domain.use_cases.UpsertHistory
import com.example.i_prep.domain.use_cases.UpsertTest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
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
    private val upsertHistory: UpsertHistory,
    private val deleteHistory: DeleteHistory,
    private val downloader: IPrepDownloader
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
                    onEvent(GlobalEvent.UpsertHistory(event.tHistory.copy(isAvailable = false)))

                    _state.update { it.copy(tHistory = emptyTHistory) }

                    onEvent(GlobalEvent.GetAllHistory)
                }
            }

            is GlobalEvent.DeleteTest -> {
                viewModelScope.launch {
                    onEvent(GlobalEvent.UpsertTest(event.pTest.copy(isAvailable = false)))

                    _state.update { it.copy(pTest = emptyPTest) }

                    onEvent(GlobalEvent.GetAllTest)
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
                        it.copy(
                            pTestList = getAllTest().first(),
                            isLoading = false
                        )
                    }
                }
            }

            is GlobalEvent.GetHistory -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    _state.update {
                        it.copy(
                            pTest = event.pTest,
                            tHistory = event.tHistory,
                            showBottomNav = false,
                            isLoading = false
                        )
                    }
                }
            }

            is GlobalEvent.GetTest -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    _state.update {
                        it.copy(showBottomNav = false, pTest = event.pTest, isLoading = false)
                    }
                }
            }

            is GlobalEvent.UpsertHistory -> {
                viewModelScope.launch {
                    upsertHistory(event.tHistory)
                    onEvent(GlobalEvent.GetAllHistory)
                }
            }

            is GlobalEvent.UpsertTest -> {
                viewModelScope.launch {

                    _state.update { it.copy(pTest = event.pTest) }

                    upsertTest(event.pTest)
                    onEvent(GlobalEvent.GetAllTest)
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

            is GlobalEvent.ShowBottomNav -> _state.update { it.copy(showBottomNav = event.isShow) }
            is GlobalEvent.CheckUpdate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val updateChangeLog = AppUpdater().checkUpdates()

                    when (updateChangeLog != null) {
                        true -> {
                            val existingFileExists = File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                updateChangeLog.url.split("/").last()
                            ).exists()

                            when (existingFileExists) {
                                true -> {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            event.context,
                                            "The latest version is downloaded and waiting for you to install.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                                false -> {
                                    when (latestVersion.compareToVersion(updateChangeLog.latestVersion)) {
                                        true -> {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    event.context,
                                                    "Downloading latest version",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            downloader.downloadFIle(
                                                url = updateChangeLog.url)
                                        }

                                        false -> {
                                            if (event.showToast) {
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(
                                                        event.context,
                                                        "App is up-to-date",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        false -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    event.context,
                                    "Failed to update",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
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
    data class UpsertHistory(val tHistory: THistory) : GlobalEvent
    data class DeleteHistory(val tHistory: THistory) : GlobalEvent

    data class SearchTest(val query: String) : GlobalEvent
    data class GetTest(val pTest: PTest) : GlobalEvent

    data class ShowBottomNav(val isShow: Boolean) : GlobalEvent

    data class GetHistory(val tHistory: THistory, val pTest: PTest) : GlobalEvent

    data class CheckUpdate(val context: Context, val showToast: Boolean) : GlobalEvent
}