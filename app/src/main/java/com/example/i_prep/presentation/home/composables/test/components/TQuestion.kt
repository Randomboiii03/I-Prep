package com.example.i_prep.presentation.home.composables.test.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TQuestion(question: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = question, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())
    }
}