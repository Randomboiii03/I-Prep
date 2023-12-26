package com.example.i_prep.presentation.home.composables.test.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TBottomBar(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    isNext: Boolean,
    isPrevious: Boolean,
    isDone: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        OutlinedButton(
            onClick = { onPrevious() },
            enabled = isPrevious,
            modifier = modifier.weight(1f)
        ) {
            Text(text = "Previous")
        }

        Button(onClick = { onNext() }, enabled = isNext, modifier = modifier.weight(1f)) {
            Text(text = if (isDone) "Done" else "Next")
        }
    }
}