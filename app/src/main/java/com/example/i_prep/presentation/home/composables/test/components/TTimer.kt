package com.example.i_prep.presentation.home.composables.test.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun TTimer(
    time: Long,
    currentIndex: Int,
    totalItems: Int,
    isTimed: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Question ${currentIndex + 1} of $totalItems",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = if (isTimed) "Time: ${formatTime(time)}" else "Time: ∞",
                style = MaterialTheme.typography.titleMedium
            )
        }

        LinearProgressIndicator(
            progress = currentIndex.toFloat() / (totalItems - 1).toFloat(),
            strokeCap = StrokeCap.Round,
            modifier = modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp)
        )
    }
}

fun formatTime(timeLeft: Long): String {
    val hours = (timeLeft / 3600000).toInt()
    val minutes = ((timeLeft % 3600000) / 60000).toInt()
    val seconds = ((timeLeft % 60000) / 1000).toInt()
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}