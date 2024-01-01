package com.example.i_prep.presentation.more.composables.about.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Update
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.i_prep.presentation.more.model.MoreNav

sealed class AboutNav(
    val title: String
) {
    object AboutList: AboutNav(title = "AboutList")

    object Version: AboutNav(title = "Version")

    object CheckUpdate: AboutNav(title = "Check for Updates")

    object PrivacyPolicy: AboutNav(title = "Privacy Policy")
}

val aboutNav = listOf(
    AboutNav.AboutList,
    AboutNav.Version,
    AboutNav.CheckUpdate,
    AboutNav.PrivacyPolicy
)