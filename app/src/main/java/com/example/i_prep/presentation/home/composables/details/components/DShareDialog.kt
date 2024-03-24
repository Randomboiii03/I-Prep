package com.example.i_prep.presentation.home.composables.details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.i_prep.common.ConnectionState
import com.example.i_prep.common.connectivityState
import com.example.i_prep.common.displayToast
import com.example.i_prep.domain.api.share.ShareAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DShareDialog(text: String, onDismiss: (Boolean) -> Unit, modifier: Modifier = Modifier) {

    var url by rememberSaveable { mutableStateOf("") }
    var isCreating by rememberSaveable { mutableStateOf(false) }
    val clipboard = LocalClipboardManager.current

    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available

    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        confirmButton = { },
        title = {
            Text(
                text = "Share Test",
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = modifier.fillMaxWidth()
            ) {
                AnimatedVisibility(visible = isCreating) {
                    CircularProgressIndicator()
                }

                AnimatedVisibility(visible = (url.isNotEmpty() && url.isNotBlank())) {
                    OutlinedTextField(
                        value = url,
                        onValueChange = {},
                        label = { Text(text = "Code") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = "Copy Code",
                                modifier = modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { clipboard.setText(AnnotatedString((url))) }
                            )
                        }
                    )
                }

                AnimatedVisibility(visible = !isCreating ) {
                    Button(onClick = {
                        coroutineScope.launch {
                            if (!isCreating && isConnected) {
                                isCreating = true

                                try {
                                    url = ShareAPI().share(text)
                                } catch (e: Exception) {
                                    displayToast("Error: $e", context)
                                }

                                isCreating = false

                            } else if (isConnected) {
                                displayToast("No internet connection", context)
                            }
                        }
                    }) {
                        Text(text = "Generate Code")
                    }
                }
            }
        })
}