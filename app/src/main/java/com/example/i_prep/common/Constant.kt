package com.example.i_prep.common

import androidx.compose.runtime.mutableStateOf
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.domain.api.model.dto.Question
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

const val userAgent = "Mozilla/5.0 (Linux; U; Android 10; SM-G960F Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/95.0.4638.50 Mobile Safari/537.36 OPR/60.0.2254.59405"

const val claudeChatUrl = "https://claude.ai/chats"
const val claudeSentryUrl = "https://claude.ai/sentry"
const val googleCPUrl = "https://accounts.google.com/gsi/client"