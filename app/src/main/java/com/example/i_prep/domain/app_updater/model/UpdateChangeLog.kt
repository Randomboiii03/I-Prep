package com.example.i_prep.domain.app_updater.model

data class UpdateChangeLog(
    val latestVersion: String,
    val latestVersionCode: Int,
    val releaseNotes: List<String>,
    val url: String
)