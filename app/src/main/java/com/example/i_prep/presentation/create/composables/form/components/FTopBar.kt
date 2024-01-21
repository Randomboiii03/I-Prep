package com.example.i_prep.presentation.create.composables.form.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.i_prep.common.githubRepo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FTopBar(onHelp: () -> Unit,modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    CenterAlignedTopAppBar(
        title = {
//            Text(text = "Create Test")
        },
//        navigationIcon = {
//            Text(
//                text = "Reset",
//                style = MaterialTheme.typography.labelLarge,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = modifier
//                    .padding(16.dp)
//                    .clickable(
//                        indication = null,
//                        interactionSource = remember { MutableInteractionSource() }
//                    ) {
//                        onReset()
//                    }
//            )
//        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                        FDropDownItem(
                            icon = Icons.Outlined.HelpOutline,
                            text = "Help",
                            onClick = {
                                expanded = !expanded
                                onHelp()
                            })

                        FDropDownItem(
                            icon = Icons.Outlined.BugReport,
                            text = "Report Bug",
                            onClick = {
                                expanded = !expanded
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(githubRepo)))
                            })
                    }
                }
            }
        })
}

@Composable
private fun FDropDownItem(icon: ImageVector, text: String, onClick: () -> Unit) {
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