package com.example.i_prep.domain.api.share.model

data class GetPaste(
    val body: String,
    val custom_url: String,
    val date_created: String,
    val edit_code: String,
    val edit_total: Int,
    val expiry: String,
    val font_name: String,
    val font_size: Int,
    val font_weight: Int,
    val option_links: String,
    val option_referrer: String,
    val option_security: String,
    val option_visibility: String,
    val response_code: Int,
    val title: String,
    val url: String,
    val view_total: Int
)