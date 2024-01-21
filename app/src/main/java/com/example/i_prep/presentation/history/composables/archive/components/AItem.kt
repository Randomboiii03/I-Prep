package com.example.i_prep.presentation.history.composables.archive.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.i_prep.R
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AITem(
    pTest: PTest,
    tHistory: THistory,
    onClickItem: (THistory) -> Unit,
    onLongItem: (THistory) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(95.dp)
            .combinedClickable(
                onLongClick = { expanded = !expanded },
                onClick = { onClickItem(tHistory) }
            ),
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
                model = ImageRequest.Builder(LocalContext.current).data(pTest.image)
                    .crossfade(true)
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

        Box(
            modifier = modifier.weight(1f)
        ) {
            Row(
                modifier = modifier
                    .wrapContentSize()
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

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = !expanded }) {
                        ADropDownItem(
                            icon = Icons.Outlined.Delete,
                            text = "Delete",
                            onClick = { onLongItem(tHistory) })
                    }
                }
            }


        }
    }
}

@Composable
private fun ADropDownItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenuItem(
        text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = text)
                Text(text = text)
            }
        },
        onClick = { onClick() },
        modifier = modifier
            .heightIn(max = 40.dp)
            .widthIn(max = 95.dp)
    )
}

fun formatDateTime(longDate: Long): String {
    val date = Date(longDate)
    val dateFormat = SimpleDateFormat(
        "dd MMMM, yyyy • hh:mm a",
        Locale.US
    ) // Locale ensures month name in English
    return dateFormat.format(date)
}