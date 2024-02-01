package com.example.i_prep.presentation.create.model

sealed class CreateNav(
    val title: String
) {
    object Form: CreateNav(title = "Form")

    object Webview: CreateNav(title = "Webview")
    object Generate: CreateNav(title = "Generate")

    object Help: CreateNav(title = "Help")
}