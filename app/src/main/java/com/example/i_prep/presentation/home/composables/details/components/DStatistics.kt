package com.example.i_prep.presentation.home.composables.details.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.randomboiii.i_prep.data.Question

@Composable
fun DStatistics(
    viewedQuestion: Int,
    totalQuestion: Int,
    correctAnswered: Int,
    testTaken: Int,
    onChange: Boolean,
    onChangeChart: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Max)
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .background(
                    if (onChange) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = .75f) else MaterialTheme.colorScheme.tertiaryContainer,
                    RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                )
                .weight(1f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onChangeChart(true) }
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Viewed\nQuestion",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = if (onChange) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onTertiaryContainer
            )

            Text(
                text = "$viewedQuestion/$totalQuestion",
                style = MaterialTheme.typography.titleLarge,
                color = if (onChange) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        Column(
            modifier = modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .weight(1f)
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Test Taken",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            Text(text = "$testTaken", style = MaterialTheme.typography.titleLarge)
        }

        Column(
            modifier = modifier
                .fillMaxHeight()
                .background(
                    if (!onChange) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = .75f) else MaterialTheme.colorScheme.tertiaryContainer,
                    RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                )
                .weight(1f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onChangeChart(false) }
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Answered Correct",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = if (!onChange) MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = .75f) else MaterialTheme.colorScheme.onTertiaryContainer,
            )

            Text(
                text = "$correctAnswered/$totalQuestion",
                style = MaterialTheme.typography.titleLarge,
                color = if (!onChange) MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = .75f) else MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
    }
}