package com.example.i_prep.domain.api.model.dto

data class TokenStream(
    val completion: String,
    val stopReason: String?,
)
