package com.example.i_prep.domain.api

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.i_prep.domain.api.model.dto.TokenStream
import com.example.i_prep.domain.api.model.payload.AppendMessagePayload
import com.example.i_prep.domain.api.model.payload.Completion
import com.example.i_prep.domain.api.model.payload.ConversationPayload
import com.example.i_prep.domain.api.model.payload.AttachmentPayload
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.gson.gson
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import java.io.Writer
import java.util.UUID

class IPrepAPI(private val cookie: String) {

    private var organizationId: String = ""

    private val gsonBuilder: GsonBuilder = GsonBuilder().apply {
        setPrettyPrinting()
        setLenient()
        setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    }

    private val gson: Gson = gsonBuilder.create()

    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 300_000 // 5 mins
        }
        install(ContentNegotiation) {
            gson() {
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

    fun getOrganizationId() {
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
                Log.v("TAG", "OrganizationId: $organizationId")
            } catch (e: Exception) {
                Log.v("TAG - getOrganizationId", "Error: $e")
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

        } catch (e: Exception) {
            Log.v("TAG - createNewChat", "Error: $e")
            return null
        }
    }

    fun getContentType(file: File) = when (file.extension) {
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

        } catch (e: Exception) {
            Log.v("TAG - uploadAttachment", "Error: $e")
            return null
        }
    }

    suspend fun sendMessage(
        conversationId: String,
        prompt: String,
        attachments: List<AttachmentPayload?>
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
                    deleteConversation("127f8743-7348-4336-8e05-45adda16babb")
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

            return message.value

        } catch (e: Exception) {
            Log.v("TAG - sendMessage", "Error: $e")
            deleteConversation("127f8743-7348-4336-8e05-45adda16babb")
            return null
        }
    }

    suspend fun deleteConversation(conversationId: String) {
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
}