package com.example.i_prep.presentation.home.composables.test.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TTopBar(title: String, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(title = {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.basicMarquee()
        )
    })
}