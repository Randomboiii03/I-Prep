package com.example.i_prep.domain.api.model.dto

data class ChatConversation(
    val created_at: String,
    val model: Any,
    val name: String,
    val summary: String,
    val updated_at: String,
    val uuid: String
)