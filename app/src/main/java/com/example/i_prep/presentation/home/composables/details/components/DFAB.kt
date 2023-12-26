package com.example.i_prep.presentation.home.composables.details.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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