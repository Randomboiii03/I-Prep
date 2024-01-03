package com.example.i_prep.presentation.more.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Update
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MoreNav(
    val title: String,
    val icon: ImageVector
) {
    object Options: MoreNav(title = "Options", icon = Icons.Outlined.Help)

    object Statistics: MoreNav(title = "Statistics", icon = Icons.Outlined.QueryStats)

    object UploadedFiles: MoreNav(title = "Uploaded Files", icon = Icons.Outlined.Folder)

    object Help: MoreNav(title = "Help", icon = Icons.Outlined.HelpOutline)

    object About: MoreNav(title = "About", icon = Icons.Outlined.Info)

    object Feedback: MoreNav(title = "Feedback", icon = Icons.Outlined.Feedback)

    object Report: MoreNav(title = "Report Bug", icon = Icons.Outlined.BugReport)
}

val moreNav = listOf(
    MoreNav.Statistics,
    MoreNav.UploadedFiles,
    MoreNav.Feedback,
    MoreNav.Report,
    MoreNav.Help,
    MoreNav.About,
)