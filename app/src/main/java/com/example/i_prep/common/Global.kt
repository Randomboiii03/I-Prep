package com.example.i_prep.common

import androidx.compose.runtime.mutableStateOf
import com.example.i_prep.presentation.create.composables.form.model.questionTypes
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

val gsonBuilder: GsonBuilder = GsonBuilder().apply {
    setPrettyPrinting()
    setLenient()
    setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
}

val gson: Gson = gsonBuilder.create()

fun String.compareToVersion(version: String): Boolean {
    val latestVersion = this.split(".").mapNotNull { it.toIntOrNull() }
    val updateVersion = version.split(".").mapNotNull { it.toIntOrNull() }

    val minLength = minOf(latestVersion.size, updateVersion.size)

    for (i in 0 until minLength) {
        val latestComponent = latestVersion[i]
        val updateComponent = updateVersion[i]

        if (latestComponent < updateComponent) {
            return true
        }
    }

    return false
}