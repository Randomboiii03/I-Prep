package com.example.i_prep.presentation.welcome.model

import com.example.i_prep.R

sealed class OnBoardingPage(
    val title: String,
    val description: String,
    val image: Int
) {
    object First: OnBoardingPage(
        title = "Prep Smarter, Ace Your Tests with I-Prep!",
        description = "AI-powered Personalized Test Preparation",
        image = R.drawable.logo
    )

    object Second: OnBoardingPage(
        title = "Prep Your Way, Not the Textbook Way",
        description = "Claude AI Tailors Your Learning Journey",
        image = R.drawable.claude
    )

    object Third: OnBoardingPage(
        title = "Reach Your Goals with Confidence",
        description = "Personalized Support Every Step of the Way!",
        image = R.drawable.trophy
    )
}

val onBoardingPage = listOf(
    OnBoardingPage.First,
    OnBoardingPage.Second,
    OnBoardingPage.Third,
)