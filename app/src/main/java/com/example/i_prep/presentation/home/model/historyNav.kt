package com.example.i_prep.presentation.home.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class HomeNav(val title: String) {
    object Library : HomeNav(title = "Library")

    object Details : HomeNav(title = "Details")

    object Test : HomeNav(title = "Test")

    object Result : HomeNav(title = "Result")
}

val homeNavs = listOf(
    HomeNav.Library,
    HomeNav.Details,
    HomeNav.Test,
    HomeNav.Result,
)