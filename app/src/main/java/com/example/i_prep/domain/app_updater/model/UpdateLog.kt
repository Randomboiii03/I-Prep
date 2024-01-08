package com.example.i_prep.domain.app_updater.model


import com.google.gson.annotations.SerializedName

data class UpdateLog(
    @SerializedName("latestVersion")
    val latestVersion: String,
    @SerializedName("url")
    val url: String
)