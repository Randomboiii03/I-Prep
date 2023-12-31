package com.example.i_prep.presentation.home.composables.details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DTopBar(
    onBack: () -> Unit,
    onModify: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Search",
                modifier = modifier
                    .padding(end = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onBack() }
            )
        },
        actions = {
            Box {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Setting",
                    modifier = modifier
                        .padding(end = 16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            expanded = !expanded
                        }
                )

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = !expanded }) {
                    DDropDownItem(
                        icon = Icons.Outlined.EditNote,
                        text = "Modify Test",
                        onClick = {
                            expanded = !expanded
                            onModify(true)
                        })

                    DDropDownItem(
                        icon = Icons.Outlined.Delete,
                        text = "Delete Test",
                        onClick = {
                            expanded = !expanded
                            onDelete()
                        })
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
private fun DDropDownItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = text)
                Text(text = text)
            }
        }, onClick = { onClick() })
}