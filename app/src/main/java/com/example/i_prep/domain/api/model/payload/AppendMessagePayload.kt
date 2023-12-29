package com.example.i_prep.domain.api.model.payload

data class AppendMessagePayload(
    val completion: Completion,
    val conversation_uuid: String,
    val organization_uuid: String,
    val text: String,
    val attachments: List<AttachmentPayload?> = emptyList(),
)

data class Completion(
    val prompt: String,
    val timezone: String = "Asia/Shanghai",
    val model: String = "claude-2.0"
)
