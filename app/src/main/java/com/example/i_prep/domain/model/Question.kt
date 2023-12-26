package com.randomboiii.i_prep.data

data class Question(
    val question: String,
    val choices: List<String> = emptyList(),
    val answer: String,
    val correct: Int = 0,
    val shown: Int = 0
)