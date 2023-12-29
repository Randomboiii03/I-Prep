package com.example.i_prep.presentation.history.model

sealed class HistoryNav(val title: String) {
    object Archive : HistoryNav(title = "Archive")

    object View : HistoryNav(title = "View")

    object Result : HistoryNav(title = "Result")
}

val historyNavs = listOf(
    HistoryNav.Archive,
    HistoryNav.View,
    HistoryNav.Result,
)