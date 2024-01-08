package com.example.i_prep.presentation.history.composables.archive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.i_prep.R
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AITem(
    pTest: PTest,
    tHistory: THistory,
    onClickItem: (THistory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(95.dp)
            .clickable { onClickItem(tHistory) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = modifier
                .weight(4f)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(pTest.image).crossfade(true)
                    .build(),
                contentDescription = pTest.title,
                placeholder = painterResource(R.drawable.logo),
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(height = 75.dp, width = 50.dp)
                    .clip(RoundedCornerShape(6.dp))
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = pTest.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formatDateTime(tHistory.dateTaken),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Row(
            modifier = modifier
                .wrapContentSize()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = modifier
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${tHistory.score}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier
                        .padding(4.dp)
                )
            }

            Box(
                modifier = modifier
                    .background(
                        MaterialTheme.colorScheme.tertiary,
                        RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${tHistory.questionsTaken}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = modifier
                        .padding(4.dp)
                )
            }
        }
    }
}

fun formatDateTime(longDate: Long): String {
    val date = Date(longDate)
    val dateFormat = SimpleDateFormat("dd MMMM, yyyy • hh:mm a", Locale.US) // Locale ensures month name in English
    return dateFormat.format(date)
}