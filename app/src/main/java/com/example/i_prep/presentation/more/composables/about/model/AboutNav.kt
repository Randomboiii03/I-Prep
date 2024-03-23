package com.example.i_prep.presentation.more.composables.about.model

sealed class AboutNav(
    val title: String
) {
    object About: AboutNav(title = "About")

    object Version: AboutNav(title = "Version")

    object CheckUpdate: AboutNav(title = "Check for Updates")

    object FAQ: AboutNav(title = "FAQ")

    object PrivacyPolicy: AboutNav(title = "Privacy Policy")
}

val aboutNav = listOf(
    AboutNav.Version,
//    AboutNav.CheckUpdate,
    AboutNav.PrivacyPolicy
)