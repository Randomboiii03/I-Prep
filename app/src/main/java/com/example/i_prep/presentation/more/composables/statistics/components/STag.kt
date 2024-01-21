package com.example.i_prep.presentation.more.composables.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkAdded
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.i_prep.presentation.GlobalState

@Composable
fun STag(globalState: GlobalState, modifier: Modifier = Modifier) {

    val pTestList = globalState.pTestList.filter { it.isAvailable }
    val tags = pTestList.flatMap { it.tags }
    val mostUsedTag = tags.groupBy { it }.mapValues { it.value.size }.maxByOrNull { it.value }?.key
    val totalUniqueTag = tags.distinct().count()

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "Tag",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(32.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ColumnStat(
                    record = "$mostUsedTag",
                    label = "Popular Tag",
                    icon = Icons.Outlined.BookmarkAdded,
                    modifier = modifier.weight(1f)
                )

                ColumnStat(
                    record = "$totalUniqueTag",
                    label = "# Unique Tags",
                    icon = Icons.Outlined.BookmarkAdd,
                    modifier = modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ColumnStat(
    record: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 6.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier.padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = record,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}