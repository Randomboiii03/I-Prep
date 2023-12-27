package com.example.i_prep.presentation.navigation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNav(
    val title: String,
    val selectedItem: ImageVector,
    val unselectedItem: ImageVector
) {
    object Home : BottomNav(
        title = "Library",
        selectedItem = Icons.Filled.CollectionsBookmark,
        unselectedItem = Icons.Outlined.CollectionsBookmark
    )

    object Create : BottomNav(
        title = "Create",
        selectedItem = Icons.Filled.Create,
        unselectedItem = Icons.Outlined.Create
    )

    object History : BottomNav(
        title = "History",
        selectedItem = Icons.Filled.History,
        unselectedItem = Icons.Outlined.History
    )

    object More : BottomNav(
        title = "More",
        selectedItem = Icons.Filled.MoreHoriz,
        unselectedItem = Icons.Outlined.MoreHoriz
    )
}

val bottomNavs = listOf(
    BottomNav.Home,
    BottomNav.Create,
    BottomNav.History,
    BottomNav.More,
)