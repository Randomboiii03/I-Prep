package com.example.i_prep.presentation.home.composables.library.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HTopBar(
    isSearch: Boolean,
    showSearch: () -> Unit,
    showFilter: () -> Unit,
    searchText: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(visible = !isSearch) {
                    Text(
                        text = "Library",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                AnimatedVisibility(visible = isSearch) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        LIconButton(
                            icon = Icons.Default.ArrowBack,
                            onAction = { showSearch() },
                            contentDesc = "Search"
                        )

                        Column(
                            modifier = modifier
                                .requiredWidth(250.dp)
                                .horizontalScroll(rememberScrollState())
                        ) {
                            BasicTextField(
                                value = searchText,
                                onValueChange = { onSearch(it) },
                                maxLines = 1,
                                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp),
                                decorationBox = { innerTextField ->
                                    Box {
                                        if (searchText.isEmpty()) {
                                            Text(
                                                text = "Search...",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        innerTextField()
                                    }
                                },
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = isSearch && searchText.isNotEmpty()) {
                    LIconButton(
                        icon = Icons.Default.Close,
                        contentDesc = "Delete Search Text",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
                        onAction = { onSearch("") })
                }
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(visible = !isSearch) {
                    LIconButton(
                        icon = Icons.Default.Search,
                        onAction = { showSearch() },
                        contentDesc = "Search",
                        modifier = modifier.padding(end = 16.dp)
                    )
                }

//                LIconButton(
//                    icon = Icons.Default.FilterList,
//                    contentDesc = "Show Filter List",
//                    onAction = { showFilter() })
//
//                LIconButton(
//                    icon = Icons.Default.MoreVert,
//                    contentDesc = "More Options",
//                    onAction = {  },
//                    modifier = modifier.padding(end = 16.dp))
            }
        }
    )
}

@Composable
private fun LIconButton(
    icon: ImageVector,
    onAction: () -> Unit,
    contentDesc: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDesc,
        tint = color,
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onAction()
            }
    )
}