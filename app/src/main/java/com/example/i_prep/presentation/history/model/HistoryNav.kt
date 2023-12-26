package com.example.i_prep.presentation.history.model

sealed class HistoryNav(val title: String) {
    object Archive : HistoryNav(title = "Archive")

    object Details : HistoryNav(title = "Details")

    object Test : HistoryNav(title = "Test")

    object Result : HistoryNav(title = "Result")
}

val historyNavs = listOf(
    HistoryNav.Archive,
    HistoryNav.Details,
    HistoryNav.Test,
    HistoryNav.Result,
)