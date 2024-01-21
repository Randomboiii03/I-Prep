package com.example.i_prep.presentation.history.composables.archive.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ATopBar() {
    TopAppBar(title = { Text(text = "History", style = MaterialTheme.typography.headlineSmall) })
}