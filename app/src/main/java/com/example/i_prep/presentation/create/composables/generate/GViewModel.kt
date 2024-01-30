package com.example.i_prep.presentation.create.composables.generate

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.i_prep.common.NotificationService
import com.example.i_prep.common.getPrompt
import com.example.i_prep.common.gson
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.domain.api.IPrepAPI
import com.example.i_prep.domain.api.model.dto.TestInfo
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.create.CState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.EOFException
import java.io.File

class GViewModel : ViewModel() {
    private val _state: MutableStateFlow<GState> = MutableStateFlow(GState())
    val state = _state

    private fun fixEscapes(message: String): String {
        val validEscapeChars = listOf('\"', '\\', 'n', 'r', 't', 'b', 'f')
        var newText = message

        for (asciiValue in 33..126) {
            val char = asciiValue.toChar()
            if (char !in validEscapeChars) {
                newText = newText.replace("\\$char", "$char", ignoreCase = true)
            }
        }

        return newText
    }

    private fun parseJson(message: String?): TestInfo? {
        val jsonData = mutableStateOf("")

        if (message != null) {
            val startIndex = message.indexOf('{')
            val endIndex = message.lastIndexOf("}") + 1
            jsonData.value = message.substring(startIndex, endIndex)

            jsonData.value = fixEscapes(jsonData.value)

            return try {
                Log.v("TAG - parseJSON", jsonData.value)
                gson.fromJson(jsonData.value, TestInfo::class.java)

            } catch (e: EOFException) {
                val modifiedJson = jsonData.value + "]}"
                Log.v("TAG - parseJSON2", modifiedJson)

                gson.fromJson(modifiedJson, TestInfo::class.java)
            }
        }

        return null
    }

    private suspend fun iPrepAPI(
        state: CState,
        cookie: String,
        globalEvent: (GlobalEvent) -> Unit,
        notification: NotificationService,
        navHostController: NavHostController
    ) {

        val api = IPrepAPI(cookie)
        var conversationId = ""

        try {
            delay(3000)
            val attachment = api.uploadAttachment(File(state.filePath))

            when (attachment != null) {
                true -> {
                    conversationId = api.createNewChat().toString()

                    val message = api.sendMessage(
                        conversationId = conversationId,
                        prompt = getPrompt(
                            version = 2,
                            questionType = state.questionType,
                            difficulty = state.difficulty,
                            language = state.language
                        ),
                        attachments = listOf(attachment)
                    )

                    var testInfo = parseJson(message)

                    when (testInfo != null && testInfo.questions.size >= 20) {
                        true -> {
                            val number =
                                Regex("\\d+").findAll(testInfo.description).map { it.value }
                                    .toList()

                            if (number.isNotEmpty()) {
                                testInfo = testInfo.copy(
                                    description = testInfo.description.replace(
                                        number[0],
                                        testInfo.questions.size.toString()
                                    )
                                )
                            }

                            val image = api.getRandomImage()

                            if (state.questionType == "tof") {
                                testInfo = testInfo.copy(
                                    questions = testInfo.questions.map {
                                        it.copy(
                                            choices = listOf(
                                                "True",
                                                "False"
                                            )
                                        )
                                    }
                                )
                            }

                            if (testInfo.questions.all { it.answer.length == 1 }) {
                                if (state.questionType == "mcq" || state.questionType == "fitb") {
                                    val maxIndex =
                                        testInfo.questions.maxOfOrNull { it.answer.toInt() }
                                    val maxChoices =
                                        testInfo.questions.maxOfOrNull { it.choices.count() }

                                    testInfo = testInfo.copy(
                                        questions = testInfo.questions.map { it.copy(answer = it.choices[it.answer.toInt() - if (maxIndex == maxChoices) 1 else 0]) }
                                    )
                                }
                            }

                            val pTest = PTest(
                                title = testInfo.title,
                                description = testInfo.description,
                                tags = testInfo.tags,
                                questionType = state.questionType,
                                questions = testInfo.questions,
                                totalItems = testInfo.questions.size,
                                language = state.language,
                                reference = state.fileName,
                                image = image,
                                dateCreated = System.currentTimeMillis(),
                            )

                            globalEvent(GlobalEvent.UpsertTest(pTest = pTest))

                            api.chatFeedback(conversationId, "nice response", "upvote")

                            notification.showNotification(
                                "${pTest.title} successfully created with ${pTest.totalItems} questions.",
                                false
                            )
                        }

                        false -> {
                            notification.showNotification(
                                "Test failed to create.\nSorry for inconvenience we will try to fix it soon. Please try again.",
                                false
                            )
                            api.deleteConversation(conversationId)
                        }
                    }
                }

                false -> notification.showNotification(
                    "No text extracted or text extracted is too short from reference file.",
                    true
                )
            }

        } catch (e: CancellationException) {
            e.printStackTrace()

        } catch (throwable: Throwable) {
            Log.v("TAG - GViewModel", "$throwable")

            if (throwable.toString().contains("com.google.gson")) {
                notification.showNotification(
                    "Test failed to parse into JSON.\nSorry for inconvenience we will try to fix it soon. Please try again.",
                    true
                )
            } else {
                notification.showNotification("Error: $throwable", true)
            }

            api.chatFeedback(conversationId, "reply of AI cannot parse into JSON", "flag/other")
        }

        withContext(Dispatchers.Main) {
            navHostController.popBackStack()
        }
    }

    fun onEvent(event: GEvent) {
        when (event) {
            is GEvent.SetState -> _state.update {
                it.copy(
                    status = event.status,
                    isLoading = event.isLoading
                )
            }

            is GEvent.ShowDialog -> _state.update { it.copy(showDialog = event.showDialog) }
            is GEvent.GenerateTest -> {
                _state.update { it.copy(status = "Create") }

                viewModelScope.launch(Dispatchers.IO) {
                    delay(3000)

                    try {
                        iPrepAPI(
                            state = event.state,
                            cookie = event.cookie,
                            globalEvent = event.globalEvent,
                            notification = event.notification,
                            navHostController = event.navHostController
                        )
                    } catch (throwable: Throwable) {
                        event.notification.showNotification(
                            "Failed to properly connect in Claude. Please try again.",
                            false
                        )
                        withContext(Dispatchers.Main) {
                            event.navHostController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}

data class GState(
    val status: String = "",
    val isLoading: Boolean = true,
    val showDialog: Boolean = false,
)

sealed interface GEvent {
    data class SetState(val status: String, val isLoading: Boolean) : GEvent
    data class ShowDialog(val showDialog: Boolean) : GEvent
    data class GenerateTest(
        val state: CState,
        val cookie: String,
        val globalEvent: (GlobalEvent) -> Unit,
        val notification: NotificationService,
        val navHostController: NavHostController
    ) : GEvent
}