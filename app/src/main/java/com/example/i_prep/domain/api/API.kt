package com.example.i_prep.domain.api

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.i_prep.common.gson
import com.example.i_prep.domain.api.model.dto.TokenStream
import com.example.i_prep.domain.api.model.payload.AppendMessagePayload
import com.example.i_prep.domain.api.model.payload.AttachmentPayload
import com.example.i_prep.domain.api.model.payload.Completion
import com.example.i_prep.domain.api.model.payload.ConversationPayload
import com.example.i_prep.domain.api.model.payload.FeedbackPayload
import com.google.gson.FieldNamingPolicy
import com.google.gson.JsonParser
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class IPrepAPI(private val cookie: String) {

    private var organizationId: String = ""
    private var chatUuid: String = ""

    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 300_000 // 5 mins
        }

        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                setLenient()
                setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            }
        }
    }.apply {
        headers {
            append(
                HttpHeaders.UserAgent,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"
            )
            append(HttpHeaders.AcceptLanguage, "en-US,en;q=0.5")
            append(HttpHeaders.ContentType, "application/json")
            append(HttpHeaders.Referrer, "https://claude.ai/chats")
            append("Sec-Fetch-Dest", "empty")
            append("Sec-Fetch-Mode", "cors")
            append("Sec-Fetch-Site", "same-origin")
            append(HttpHeaders.Connection, "keep-alive")
        }
    }

    init {
        getOrganizationId()
    }

    private fun getOrganizationId() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.get("https://claude.ai/api/organizations") {
                    headers {
                        append(HttpHeaders.Cookie, cookie)
                    }
                }

                if (!response.status.isSuccess()) {
                    Log.v("TAG - getOrganizationId", "Error!")
                    return@launch
                }

                val responseData = response.bodyAsText()
                organizationId =
                    JsonParser.parseString(responseData).asJsonArray[0].asJsonObject.get("uuid").asString

            } catch (throwable: Throwable) {
                Log.v("TAG - getOrganizationId", "Error: $throwable")
            }
        }
    }

    private fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }

    suspend fun createNewChat(): String? {
        try {
            val response =
                client.post("https://claude.ai/api/organizations/$organizationId/chat_conversations") {
                    headers {
                        append(HttpHeaders.Cookie, cookie)
                    }
                    contentType(ContentType.Application.Json)
                    setBody(ConversationPayload(uuid = generateUuid(), name = ""))
                }

            if (!response.status.isSuccess()) {
                Log.v("TAG - createNewChat", "Error: ${response.bodyAsText()}")
                return null
            }

            val responseData = response.bodyAsText()

            return JsonParser.parseString(responseData).asJsonObject.get("uuid").asString

        } catch (throwable: Throwable) {
            Log.v("TAG - createNewChat", "Error: $throwable")
            return null
        }
    }

    private fun getContentType(file: File) = when (file.extension) {
        "pdf" -> "application/pdf"
        "txt" -> "text/plain"
        "csv" -> "text/csv"
        else -> "application/octet-stream"
    }

    @OptIn(InternalAPI::class)
    suspend fun uploadAttachment(file: File): AttachmentPayload? {
        try {
            if (file.extension == "txt") {
                return AttachmentPayload(
                    file_name = file.name,
                    file_size = file.length(),
                    extracted_content = file.readText(Charsets.UTF_8)
                )
            }

            val multipartData = MultiPartFormDataContent(
                formData {
                    append("file", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                        append(HttpHeaders.ContentType, getContentType(file))
                    })
                    append("orgUuid", organizationId)
                }
            )

            val response = client.post("https://claude.ai/api/convert_document") {
                headers {
                    append(HttpHeaders.Cookie, cookie)
                }
                body = multipartData
            }

            return gson.fromJson(response.bodyAsText(), AttachmentPayload::class.java)

        } catch (throwable: Throwable) {
            Log.v("TAG - uploadAttachment", "Error: $throwable")
            return null
        }
    }

    suspend fun sendMessage(
        conversationId: String,
        prompt: String,
        attachments: List<AttachmentPayload?> = emptyList()
    ): String? {
        try {
            val message = mutableStateOf("")

            client.preparePost("https://claude.ai/api/append_message") {
                headers {
                    append(HttpHeaders.Accept, "text/event-stream, text/event-stream")
                    append(HttpHeaders.Referrer, "https://claude.ai/chats")
                    append(HttpHeaders.Origin, "https://claude.ai")
                    append(HttpHeaders.Cookie, cookie)
                }

                setBody(
                    gson.toJson(
                        AppendMessagePayload(
                            completion = Completion(prompt = prompt),
                            conversation_uuid = conversationId,
                            organization_uuid = organizationId,
                            text = prompt,
                            attachments = attachments
                        )
                    )
                )
            }.execute { response ->
                if (!response.status.isSuccess()) {
                    Log.v("TAG - sendMessage", "Error!")
                    return@execute
                }

                val channel: ByteReadChannel = response.bodyAsChannel()
                while (!channel.isClosedForRead) {
                    val lineStr = channel.readUTF8Line() ?: ""
                    if (lineStr.isNotEmpty()) {
                        val dataJson = lineStr.substring("data: ".length)
                        val tokenDto = gson.fromJson(dataJson, TokenStream::class.java)
                        val tidyJson = gson.toJson(tokenDto)
                        message.value += String(
                            JsonParser.parseString(tidyJson).asJsonObject.get("completion").asString.toByteArray(
                                Charsets.UTF_8
                            ), Charsets.UTF_8
                        )
                    }
                }
            }

            getChatConversation(conversationId)

            return message.value

        } catch (throwable: Throwable) {
            Log.v("TAG - sendMessage", "Error: $throwable")
            return null
        }
    }

    suspend fun getChatConversation(conversationId: String) {
        try {
            val response =
                client.get("https://claude.ai/api/organizations/$organizationId/chat_conversations/$conversationId") {
                    headers {
                        append(HttpHeaders.Referrer, "https://claude.ai/chats/$conversationId")
                        append(HttpHeaders.Origin, "https://claude.ai")
                        append(HttpHeaders.Cookie, cookie)
                    }
                }

            if (response.status.isSuccess()) {
                val responseData = response.bodyAsText()
                val chatMessages = JsonParser.parseString(responseData).asJsonObject.get("chat_messages").asJsonArray
                chatUuid = chatMessages.find { it.asJsonObject.get("sender").asString == "assistant" }?.asJsonObject?.get("uuid")?.asString ?: ""
            }

        } catch (throwable: Throwable) {
            Log.v("TAG - getChatConversation", "Error: $throwable")
        }
    }

    suspend fun chatFeedback(conversationId: String, reason: String, type: String) {
        delay(1000)

        client.preparePost("https://claude.ai/api/organizations/$organizationId/chat_conversations/$conversationId/chat_messages/$chatUuid/chat_feedback") {
            headers {
                append(HttpHeaders.AcceptCharset, "*/*")
                append(HttpHeaders.Referrer, "https://claude.ai/chats/$conversationId")
                append(HttpHeaders.Origin, "https://claude.ai")
                append(HttpHeaders.Cookie, cookie)
            }
            contentType(ContentType.Application.Json)
            setBody(gson.toJson(FeedbackPayload(reason, type)))
        }.execute { response ->
            if (response.status.isSuccess()) {
                deleteConversation(conversationId)
            }
        }
    }

    suspend fun deleteConversation(conversationId: String) {
        delay(1000)

        val response =
            client.delete("https://claude.ai/api/organizations/$organizationId/chat_conversations/$conversationId") {
                headers {
                    append(HttpHeaders.Cookie, cookie)
                }
            }

        if (!response.status.isSuccess()) {
            Log.v("TAG - deleteConversation", "Error!")
        }
    }

    suspend fun getRandomImage(): String {
        return HttpClient().get("https://random.imagecdn.app/v1/image?width=400&height=600&category=nature")
            .bodyAsText()
    }
}