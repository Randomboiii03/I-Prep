package com.example.i_prep.presentation.create.composables.generate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Generate(modifier: Modifier = Modifier) {
    Scaffold { padddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "GENERATE")
        }
    }
}