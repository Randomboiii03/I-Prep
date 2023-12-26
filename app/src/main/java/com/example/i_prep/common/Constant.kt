package com.example.i_prep.common

import androidx.compose.runtime.mutableStateOf
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import com.randomboiii.i_prep.data.Question
import kotlin.random.Random

fun dummyQuestions(item: Int): List<Question> {
    val questions = mutableStateOf(emptyList<Question>())
    for (i in 1..item) {
        val num = Random.nextInt(1, 5)
        questions.value += Question(
            question = "Question $i",
            answer = "Choice $num",
            choices = listOf("Choice 1", "Choice 2", "Choice 3", "Choice 4")
        )
    }

    return questions.value
}

val emptyQuestion: Question = Question("", emptyList(), "", 0, 0)
val emptyPTest: PTest = PTest(
    0,
    "",
    "",
    emptyList(),
    "",
    emptyList(),
    0,
    "",
    "",
    "https://picsum.photos/400/600",
    0L,
    true,
    0
)
val emptyTHistory: THistory = THistory(0, 0, emptyList(), emptyList(), 0, 0, 0)