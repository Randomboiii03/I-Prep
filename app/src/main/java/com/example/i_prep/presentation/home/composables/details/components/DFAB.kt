package com.example.i_prep.presentation.home.composables.details.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DFAB(onClickFAB: () -> Unit) {
    ExtendedFloatingActionButton(
        text = { Text(text = "Start") },
        icon = {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Start"
            )
        }, onClick = { onClickFAB() })
}