package com.example.i_prep.domain.api.model.payload

data class AttachmentPayload(
    val file_name: String,
    val file_type: String = "text/plain",
    val file_size: Long,
    val extracted_content: String,
    val totalPages: Int? = null
)