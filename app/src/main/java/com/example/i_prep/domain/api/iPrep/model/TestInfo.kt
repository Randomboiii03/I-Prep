package com.example.i_prep.domain.api.iPrep.model

data class TestInfo(
    val title: String,
    val description: String,
    val tags: List<String>,
    val questions: List<Question>,
    val image: String
)