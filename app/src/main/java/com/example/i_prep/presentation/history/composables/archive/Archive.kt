package com.example.i_prep.presentation.history.composables.archive

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.i_prep.common.emptyTHistory
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.history.composables.archive.components.AITem
import com.example.i_prep.presentation.history.composables.archive.components.ATopBar

@Composable
fun Archive(
    globalState: GlobalState,
    globalEvent: (GlobalEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
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
                    Column {
                        LazyColumn {
                            items(
                                items = globalState.tHistoryList,
                                key = { it.testId }
                            ) { item ->
                                val pTest = globalState.pTestList.find { it.testId == item.testId }

                                AITem(pTest = pTest!!, tHistory = item, onClickItem = {})
                            }
                        }
                    }
                }
            }
        }
    }
}