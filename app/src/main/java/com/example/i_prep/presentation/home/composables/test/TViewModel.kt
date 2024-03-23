package com.example.i_prep.presentation.home.composables.test

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.i_prep.common.emptyPTest
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.domain.api.model.Question
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.home.model.HomeNav
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TViewModel : ViewModel() {
    private var timer: CountDownTimer? = null

    private val _state: MutableStateFlow<TState> = MutableStateFlow(TState())
    val state: StateFlow<TState> = _state

    private fun getQuestionList(pTest: PTest): List<Question> {
        val questionList = mutableStateOf(emptyList<Question>())

        for (item in pTest.questions.shuffled()) {
            if (questionList.value.size == pTest.itemSet) {
                break
            }

            val shuffledChoices = item.choices.shuffled()
            val shuffledItem = item.copy(choices = shuffledChoices)
            questionList.value += shuffledItem
        }

        return questionList.value
    }

//    private fun getQuestionList(pTest: PTest): List<Question> {
//        val sortedQuestions = if (pTest.questions.any { it.shown > 0 }) {
//            pTest.questions.shuffled().sortedBy { it.shown }
//        } else {
//            pTest.questions.shuffled()
//        }
//
//        return sortedQuestions.take(pTest.itemSet)
//            .map { it.copy(choices = it.choices.shuffled()) }
//    }

    private fun initializeAnswers(items: Int): List<String> {
        return List(items) { "" }
    }

    private fun startTimer(
        navHostController: NavHostController,
        globalEvent: (GlobalEvent) -> Unit
    ) {
        stopTimer()
        // adjust it for testing = 1000L, actual value = 30000L
        timer = object : CountDownTimer((state.value.pTest.itemSet * 30000L) + 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                _state.update { it.copy(time = millisUntilFinished) }
            }

            override fun onFinish() {
                onEvent(TEvent.CheckResult(navHostController, globalEvent))
            }
        }

        timer?.start()
    }

    fun stopTimer() {
        timer?.cancel()
    }

    fun onEvent(event: TEvent) {
        when (event) {
            is TEvent.CheckResult -> {
                stopTimer()

                val questions = state.value.pTest.questions.toMutableList()

                val score = state.value.questions.mapIndexed { index, question ->
                    questions.indexOfFirst { it.question == question.question }
                        .takeIf { it != -1 }
                        ?.let { indexQ ->
                            questions[indexQ] = questions[indexQ].copy(
                                shown = questions[indexQ].shown + 1,
                                correct = questions[indexQ].correct + if (question.answer.lowercase() == state.value.answers[index].lowercase()) 1 else 0
                            )
                            if (question.answer.lowercase() == state.value.answers[index].lowercase()) 1 else 0
                        } ?: 0
                }.sum()

                _state.update {
                    it.copy(
                        pTest = state.value.pTest.copy(questions = questions),
                        score = score
                    )
                }

                viewModelScope.launch {
                    event.globalEvent(
                        GlobalEvent.UpsertHistory(
                            tHistory = THistory(
                                testId = state.value.pTest.testId,
                                questions = state.value.questions,
                                selectedAnswer = state.value.answers,
                                questionsTaken = state.value.pTest.itemSet,
                                score = state.value.score,
                                dateTaken = System.currentTimeMillis()
                            )
                        )
                    )

                    event.globalEvent(GlobalEvent.UpsertTest(state.value.pTest))

                    withContext(Dispatchers.Main) {
                        event.navHostController.navigate(HomeNav.Result.title) {
                            popUpTo(HomeNav.Library.title)
                        }
                    }
                }
            }

            is TEvent.InitializeTest -> {
                _state.update {
                    it.copy(
                        pTest = event.pTest,
                        questions = getQuestionList(event.pTest),
                        answers = initializeAnswers(event.pTest.itemSet),
                        currentQIndex = 0,
                        score = -1,
                        isLoading = false
                    )
                }

                if (event.pTest.isTimed) startTimer(event.navHostController, event.globalEvent)
            }

            is TEvent.InsertAnswer -> {
                val answerList = state.value.answers.toMutableList()
                answerList[state.value.currentQIndex] = event.answer

                _state.update { it.copy(answers = answerList) }
            }

            TEvent.NextQuestion -> {
                if (state.value.pTest.itemSet > state.value.currentQIndex - 1) {
                    _state.update { it.copy(currentQIndex = state.value.currentQIndex + 1) }
                }
            }

            TEvent.PreviousQuestion -> {
                if (state.value.currentQIndex > 0) {
                    _state.update { it.copy(currentQIndex = state.value.currentQIndex - 1) }
                }
            }
        }
    }
}

data class TState(
    val pTest: PTest = emptyPTest,
    val questions: List<Question> = emptyList(),
    val answers: List<String> = emptyList(),
    val currentQIndex: Int = 0,
    val time: Long = 0L,
    val score: Int = -1,
    val isLoading: Boolean = true
)

sealed interface TEvent {
    data class InitializeTest(
        val pTest: PTest,
        val navHostController: NavHostController,
        val globalEvent: (GlobalEvent) -> Unit
    ) : TEvent

    data class InsertAnswer(val answer: String) : TEvent

    object PreviousQuestion : TEvent
    object NextQuestion : TEvent
    data class CheckResult(
        val navHostController: NavHostController,
        val globalEvent: (GlobalEvent) -> Unit
    ) : TEvent
}