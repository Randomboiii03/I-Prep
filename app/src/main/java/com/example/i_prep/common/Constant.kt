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

const val updateJSON = "https://raw.githubusercontent.com/Randomboiii03/I-Prep/master/update-changelog.json"
const val latestVersion = "1.0.0"

const val googleForm = "https://forms.gle/dqFPQ5un5FHfYftr8"

const val claudeAI = "https://support.anthropic.com/en/collections/4078531-claude-ai"

const val userAgent = "Mozilla/5.0 (Linux; U; Android 10; SM-G960F Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/95.0.4638.50 Mobile Safari/537.36 OPR/60.0.2254.59405"

const val claudeChatUrl = "https://claude.ai/chats"
const val claudeSentryUrl = "https://claude.ai/sentry"
const val googleCPUrl = "https://accounts.google.com/gsi/client"

const val about1 = """
    I-Prep is a personalized test prep application which is integrated with Claude AI from Anthropic via a reversed engineered web API. This SERVICE is provided at no cost and is intended for use as is but with proper and responsible use is advised.
    
    This page details our policies with the use and disclosure of affiliation with Anthropic.
    
    If you choose to use our Service, then you agree to the use of your personal/owned account for signing-in to get access for Claude AI. We will not collect any information upon or during use of our Service. All documents used and uploaded to create the test will not be our responsibility, all will be under the Anthropics’ Terms of Use and Privacy Policy. """

const val about2_1 = """
    For integrating Claude AI for our Service, we may require you to sign-in your personal/owned account. With this, we will use the cookies upon signing-in to use in our reverse engineered API to connect and use Claude. The accounts and uploaded documents that have been used will not be collected as described in this privacy policy.""""
const val about2_2 = """
    Links to the privacy policy of third-party service provides used by the app:"""

const val claudePrivPol = "https://console.anthropic.com/legal/privacy"

const val about3 = """
    We may periodically update our Privacy Policy. Thus, you are advised to review this page periodically for any changes."""

const val about4 = """
    If you have any questions or suggestions about this Privacy Policy, do not hesitate to reach out to us on """

const val githubRepo = "https://github.com/Randomboiii03/I-Prep/issues"