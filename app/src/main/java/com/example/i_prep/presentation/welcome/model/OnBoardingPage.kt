package com.example.i_prep.presentation.welcome.model

sealed class OnBoardingPage(
    val title: String,
    val description: String
) {
    object First: OnBoardingPage(
        title = "Title 1",
        description = "Description 1"
    )

    object Second: OnBoardingPage(
        title = "Title 2",
        description = "Description 2"
    )

    object Third: OnBoardingPage(
        title = "Title 3",
        description = "Description 3"
    )
}

val onBoardingPage = listOf(
    OnBoardingPage.First,
    OnBoardingPage.Second,
    OnBoardingPage.Third,
)