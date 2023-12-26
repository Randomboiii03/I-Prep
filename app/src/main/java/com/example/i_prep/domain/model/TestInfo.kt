package com.randomboiii.i_prep.data

data class TestInfo(
    val id: String,
    val title: String,
    val description: String,
    val tags: List<String>,
    val language: String,
    val questionType: String,
    val questions: List<Question>,
    val totalItems: Int,
    val reference: String,
    val dateCreated: String,
    val isError: Boolean,
    val errorMessage: String?,
)