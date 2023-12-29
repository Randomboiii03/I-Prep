package com.example.i_prep.presentation.home.model

sealed class HomeNav(val title: String) {
    object Library : HomeNav(title = "Library")

    object Details : HomeNav(title = "Details")

    object Test : HomeNav(title = "Test")

    object Result : HomeNav(title = "Result")

    object View : HomeNav(title = "View")
}

val homeNavs = listOf(
    HomeNav.Library,
    HomeNav.Details,
    HomeNav.Test,
    HomeNav.Result,
    HomeNav.View
)