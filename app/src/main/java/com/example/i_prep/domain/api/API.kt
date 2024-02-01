package com.example.i_prep.domain.api

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.i_prep.common.gson
import com.example.i_prep.domain.api.model.payload.AppendMessagePayload
import com.example.i_prep.domain.api.model.payload.AttachmentPayload
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
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.delay
import java.io.File
import java.util.UUID

class IPrepAPI(private val cookie: String) {

//    private var organizationId: String = ""
    var chatUuid: String = ""

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

    suspend fun getOrganizationId(): String? {
        try {
            val response = client.get("https://claude.ai/api/organizations") {
                headers {
                    append(HttpHeaders.Cookie, cookie)
                }
            }

            if (!response.status.isSuccess()) {
                Log.v("TAG - getOrganizationId", "Error!")
                return null
            }

            val responseData = response.bodyAsText()

            return JsonParser.parseString(responseData).asJsonArray[0].asJsonObject.get("uuid").asString

        } catch (throwable: Throwable) {
            Log.v("TAG - getOrganizationId", "Error: $throwable")
            return null
        }
    }

    private fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }

    suspend fun createNewChat(organizationId: String): String? {
        try {
            val response = client.post("https://claude.ai/api/organizations/$organizationId/chat_conversations") {
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
    suspend fun uploadAttachment(organizationId: String, file: File): AttachmentPayload? {
        try {
            if (file.extension == "txt") {
                return AttachmentPayload(
                    file_name = file.name,
                    file_size = file.length(),
                    extracted_content = file.readText(Charsets.UTF_8),
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
                    append(HttpHeaders.Referrer, "https://claude.ai/chat/$organizationId")
                    append(HttpHeaders.Origin, "https://claude.ai")
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
        organizationId: String,
        conversationId: String,
        prompt: String,
        attachments: List<AttachmentPayload?> = emptyList()
    ): Boolean {
        try {
            val status = mutableStateOf(false)

            client.preparePost("https://claude.ai/api/organizations/$organizationId/chat_conversations/$conversationId/completion") {
                headers {
                    append(HttpHeaders.Accept, "text/event-stream, text/event-stream")
                    append(HttpHeaders.Referrer, "https://claude.ai/chat/$conversationId")
                    append(HttpHeaders.Origin, "https://claude.ai")
                    append(HttpHeaders.Cookie, cookie)
                }
                contentType(ContentType.Application.Json)
                setBody(
                    gson.toJson(
                        AppendMessagePayload(
                            prompt = prompt,
                            attachments = attachments
                        )
                    )
                )
            }.execute { response ->
                Log.v("TAG - sendMessage", response.bodyAsText())
                if (!response.status.isSuccess()) {
                    Log.v("TAG - sendMessage", "Error!")
                    return@execute false
                }

                status.value = response.status.isSuccess()
            }

            return status.value

        } catch (throwable: Throwable) {
            Log.v("TAG - sendMessage", "Error: $throwable")
            return false
        }
    }

    suspend fun getChatConversation(organizationId: String, conversationId: String): String? {
        val message = mutableStateOf("")

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
                message.value = chatMessages.find { it.asJsonObject.get("sender").asString == "assistant" }?.asJsonObject?.get("text")?.asString ?: ""

            } else {
                return null
            }

            return message.value

        } catch (throwable: Throwable) {
            Log.v("TAG - getChatConversation", "Error: $throwable")
            return null
        }
    }

    suspend fun chatFeedback(organizationId: String, conversationId: String, reason: String, type: String) {
        delay(2000)

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
                deleteConversation(organizationId, conversationId)
            }
        }
    }

    suspend fun deleteConversation(organizationId: String, conversationId: String) {
        delay(500)

        val response =
            client.delete("https://claude.ai/api/organizations/$organizationId/chat_conversations/$conversationId") {
                headers { append(HttpHeaders.Cookie, cookie) }
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