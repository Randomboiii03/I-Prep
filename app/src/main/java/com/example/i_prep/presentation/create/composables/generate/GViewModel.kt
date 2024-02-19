package com.example.i_prep.presentation.create.composables.generate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import co.yml.charts.common.extensions.isNotNull
import com.example.i_prep.common.NotificationService
import com.example.i_prep.common.emptyPTest
import com.example.i_prep.common.getPrompt
import com.example.i_prep.common.gson
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.domain.api.IPrepAPI
import com.example.i_prep.domain.api.model.dto.TestInfo
import com.example.i_prep.domain.api.model.payload.AttachmentPayload
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.create.CState
import com.google.gson.JsonSyntaxException
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

    private suspend fun iPrepAPI(
        state: CState
    ) {
        val organizationId: String?
        val conversationId: String?
        val isSuccess: Boolean
        var message: String?
        var jsonData: String
        var testInfo: TestInfo? = null
        var isError = true

        val api = IPrepAPI(state.cookie)
        organizationId = api.getOrganizationId()

        if (organizationId != null) {
            onEvent(GEvent.SetStatus("Organization Id acquired!"))

            val attachment: AttachmentPayload? =
                api.uploadAttachment(organizationId, File(state.filePath))

            when (attachment.isNotNull()) {
                true -> {
                    onEvent(GEvent.SetStatus("Attachment uploaded successful!"))
                    conversationId = api.createNewChat(organizationId)

                    if (conversationId != null) {
                        onEvent(GEvent.SetStatus("Conversation field created!"))

                        delay(500)
                        onEvent(GEvent.SetStatus("Conversing to Claude AI!"))

                        isSuccess = api.sendMessage(
                            organizationId = organizationId,
                            conversationId = conversationId,
                            prompt = getPrompt(
                                version = 2,
                                questionType = state.questionType,
                                difficulty = state.difficulty,
                                language = state.language
                            ),
                            attachments = listOf(attachment)
                        )

                        when (isSuccess) {
                            true -> {
                                onEvent(GEvent.SetStatus("Retrieving conversation!"))
                                message = api.getChatConversation(organizationId, conversationId)

                                if (message != null && message != "") {
                                    onEvent(GEvent.SetStatus("Parsing conversation from JSON!"))
                                    
                                    val startIndex = message.indexOf('{')
                                    val endIndex = message.lastIndexOf("}") + 1
                                    jsonData = message.substring(startIndex, endIndex)

                                    jsonData = fixEscapes(jsonData)

                                    try {
                                        testInfo = try {
                                            Log.v("TAG - parseJSON", jsonData)
                                            gson.fromJson(jsonData, TestInfo::class.java)

                                        } catch (e: JsonSyntaxException) {
                                            if (e.cause is EOFException) {
                                                val modifiedJson = "$jsonData]}"
                                                Log.v(
                                                    "TAG - parseJSON2",
                                                    "Fixed JSON: $modifiedJson"
                                                )
                                                gson.fromJson(modifiedJson, TestInfo::class.java)
                                            } else {
                                                Log.v("TAG - parseJSON2", "Error parsing JSON: $e")
                                                throw e  // Rethrow to allow further handling
                                            }
                                        }
                                    } catch (throwable: Throwable) {
                                        Log.v("TAG - parseJSON2", "Error: $throwable")
                                    }

                                    if (testInfo != null && testInfo.questions.size > 20) {
                                        onEvent(GEvent.SetStatus("Saving test to database!"))

                                        try {
                                            val number = Regex("\\d+").findAll(testInfo.description)
                                                .map { it.value }.toList()

                                            if (number.isNotEmpty()) {
                                                testInfo = testInfo.copy(
                                                    description = testInfo.description.replace(
                                                        number[0],
                                                        testInfo.questions.size.toString()
                                                    )
                                                )
                                            }
                                        } catch (throwable: Throwable) {
                                            Log.v("TAG - parseJSON2", "Error: $throwable")
                                        }

                                        val image = api.getRandomImage()

                                        if (testInfo != null) {
                                            if (state.questionType == "tof") {
                                                testInfo =
                                                    testInfo.copy(questions = testInfo.questions.map {
                                                        it.copy(choices = listOf("True", "False"))
                                                    })
                                            }

                                            if (testInfo.questions.all { it.answer.length == 1 }) {
                                                if (state.questionType == "mcq" || state.questionType == "fitb") {
                                                    val maxIndex =
                                                        testInfo.questions.maxOfOrNull { it.answer.toInt() }
                                                    val maxChoices =
                                                        testInfo.questions.maxOfOrNull { it.choices.count() }

                                                    testInfo =
                                                        testInfo.copy(questions = testInfo.questions.map {
                                                            it.copy(answer = it.choices[it.answer.toInt() - if (maxIndex == maxChoices) 1 else 0])
                                                        })
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

                                            isError = false

                                            onEvent(
                                                GEvent.ShowNotification(
                                                    message = "${pTest.title} successfully created with ${pTest.totalItems} questions.",
                                                    isError = isError,
                                                    pTest = pTest
                                                )
                                            )

                                        } else onEvent(
                                            GEvent.ShowNotification(
                                                message = "Test failed to save in database. Please try again.",
                                                isError = isError
                                            )
                                        )

                                    } else onEvent(
                                        GEvent.ShowNotification(
                                            message = "Test failed parse JSON. Please try again.",
                                            isError = isError
                                        )
                                    )

                                } else GEvent.ShowNotification(
                                    message = "Failed to retrieve message. Please try again.",
                                    isError = isError
                                )

                                api.chatFeedback(
                                    organizationId,
                                    conversationId,
                                    if (isError) "JSON is broken, cannot be used please improve it. Thanks" else "nice response",
                                    if (isError) "flag/other" else "upvote"
                                )
                            }

                            false -> {

                                api.deleteConversation(organizationId, conversationId)

                                onEvent(
                                    GEvent.ShowNotification(
                                        message = "Failed to converse with Claude AI. You may have exceed your used limit.\nPlease try again after an hour or more.\n${api.result}",
                                        isError = isError
                                    )
                                )
                            }
                        }

                    } else {
                        onEvent(
                            GEvent.ShowNotification(
                                message = "Failed to create chat conversation.\nPlease make sure to have stable internet connection and please try again.",
                                isError = true
                            )
                        )
                    }
                }

                false -> {
                    onEvent(
                        GEvent.ShowNotification(
                            message = "Failed to upload reference file.\nPlease make sure to have stable internet connection and please try again.",
                            isError = true
                        )
                    )
                }
            }
        } else {
            onEvent(
                GEvent.ShowNotification(
                    message = "Failed to connect on Antropic's server.\nPlease make sure to have stable internet connection and please try again.",
                    isError = true
                )
            )
        }
    }

    fun onEvent(event: GEvent) {
        when (event) {
            is GEvent.SetStatus -> _state.update { it.copy(status = event.status) }
            is GEvent.ShowDialog -> _state.update { it.copy(showDialog = event.showDialog) }
            is GEvent.ShowNotification -> {
                _state.update {
                    it.copy(
                        showNotification = event.showNotification,
                        message = event.message,
                        isError = event.isError,
                        pTest = event.pTest
                    )
                }
            }

            is GEvent.GenerateTest -> {
                if (state.value.status == "") {
                    onEvent(GEvent.SetStatus("Starting to generate"))

                    viewModelScope.launch(Dispatchers.IO) {
                        delay(3000)

                        try {
                            Log.v("TAG", "API running!")
                            iPrepAPI(state = event.state)
                        } catch (throwable: Throwable) {
                            Log.v("TAG - GenerateTest", "Error: $throwable")
                            onEvent(
                                GEvent.ShowNotification(
                                    message = "Error: $throwable",
                                    isError = true
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

data class GState(
    val status: String = "",
    val showDialog: Boolean = false,
    val showNotification: Boolean = false,
    val message: String = "",
    val isError: Boolean = false,
    val pTest: PTest = emptyPTest
)

sealed interface GEvent {
    data class SetStatus(val status: String) : GEvent
    data class ShowDialog(val showDialog: Boolean) : GEvent
    data class ShowNotification(
        val showNotification: Boolean = true,
        val message: String,
        val isError: Boolean,
        val pTest: PTest = emptyPTest
    ) : GEvent

    data class GenerateTest(val state: CState) : GEvent
}