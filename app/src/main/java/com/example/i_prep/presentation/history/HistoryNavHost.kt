package com.example.i_prep.presentation.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.history.composables.archive.Archive
import com.example.i_prep.presentation.history.composables.view.VEvent
import com.example.i_prep.presentation.history.composables.view.VVIewModel
import com.example.i_prep.presentation.history.composables.view.mc.ViewMC
import com.example.i_prep.presentation.history.model.HistoryNav
import com.example.i_prep.presentation.home.composables.result.Result

@Composable
fun HistoryNavHost(
    globalState: GlobalState,
    globalEvent: (GlobalEvent) -> Unit
) {
    val historyNavHostController = rememberNavController()

    val mVViewModel = viewModel<VVIewModel>()
    val onEvent = mVViewModel::onEvent

    NavHost(navController = historyNavHostController, startDestination = HistoryNav.Archive.title) {
        composable(route = HistoryNav.Archive.title) {
            Archive(
                globalState = globalState,
                globalEvent = globalEvent,
                navHostController = historyNavHostController
            )
        }

        composable(route = HistoryNav.View.title) {
            LaunchedEffect(true) {
                onEvent(
                    VEvent.InitializeResult(
                        pTest = globalState.pTest,
                        tHistory = globalState.tHistory
                    )
                )
            }

            when(globalState.pTest.questionType != "sa") {
                true -> ViewMC(mVVIewModel = mVViewModel, onEvent = onEvent, navHostController = historyNavHostController)
                false -> TODO()
            }
        }

        composable(HistoryNav.Result.title) {
            val state by mVViewModel.state.collectAsState()

            Result(
                score = state.tHistory.score,
                itemSet = state.tHistory.questionsTaken,
                navHostController = historyNavHostController
            )
        }
    }
}