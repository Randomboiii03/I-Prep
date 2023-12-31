package com.example.i_prep.presentation.history.composables.archive

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.i_prep.common.emptyPTest
import com.example.i_prep.common.emptyTHistory
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.history.composables.archive.components.AITem
import com.example.i_prep.presentation.history.composables.archive.components.ATopBar
import com.example.i_prep.presentation.history.model.HistoryNav

@Composable
fun Archive(
    globalState: GlobalState,
    globalEvent: (GlobalEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(true))
    }

    Scaffold(
        topBar = { ATopBar() }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (globalState.isLoading) {
                true -> {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                false -> {
                    when (globalState.tHistoryList.isEmpty()) {
                        true -> {
                            Box(
                                modifier = modifier
                                    .fillMaxSize()
                                    .navigationBarsPadding(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No history available yet",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        false -> {
                            Column(
                                modifier = modifier
                                    .fillMaxSize()
                                    .navigationBarsPadding()
                                    .padding(bottom = 20.dp)
                            ) {
                                LazyColumn {
                                    items(
                                        items = globalState.tHistoryList.reversed(),
                                        key = { it.historyId }
                                    ) { item ->
                                        val pTest =
                                            globalState.pTestList.find { it.testId == item.testId } ?: emptyPTest

                                        AITem(
                                            pTest = pTest!!,
                                            tHistory = item,
                                            onClickItem = {
                                                globalEvent(
                                                    GlobalEvent.GetHistory(
                                                        tHistory = item,
                                                        pTest = pTest
                                                    )
                                                )

                                                navHostController.navigate(HistoryNav.View.title) {
                                                    popUpTo(HistoryNav.Archive.title)
                                                }
                                            })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}