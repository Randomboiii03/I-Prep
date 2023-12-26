package com.example.i_prep.presentation.history

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.history.composables.archive.Archive
import com.example.i_prep.presentation.history.model.HistoryNav

@Composable
fun HistoryNavHost(
    globalState: GlobalState,
    globalEvent: (GlobalEvent) -> Unit
) {
    val historyNavHostController = rememberNavController()

    NavHost(navController = historyNavHostController, startDestination = HistoryNav.Archive.title) {
        composable(route = HistoryNav.Archive.title) {
            Archive(
                globalState = globalState,
                globalEvent = globalEvent,
                navHostController = historyNavHostController
            )
        }
    }
}