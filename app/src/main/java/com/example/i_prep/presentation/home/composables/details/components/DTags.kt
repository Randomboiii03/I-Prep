package com.example.i_prep.presentation.home.composables.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DTags(tags: List<String>, modifier: Modifier = Modifier) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(tags) { tag ->
            SuggestionChip(
                onClick = { /*TODO*/ },
                label = { Text(text = tag, style = MaterialTheme.typography.bodySmall) },
//                colors = SuggestionChipDefaults.elevatedSuggestionChipColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    labelColor = MaterialTheme.colorScheme.onSurface
//                ),
//                border = SuggestionChipDefaults.suggestionChipBorder(
//                    borderColor = MaterialTheme.colorScheme.onSurface,
//                    borderWidth = 1.5f.dp
//                )
            )
        }
    }
}