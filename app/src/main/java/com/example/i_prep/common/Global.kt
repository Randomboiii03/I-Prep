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