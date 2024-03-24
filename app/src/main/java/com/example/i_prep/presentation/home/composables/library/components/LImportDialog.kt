package com.example.i_prep.presentation.home.composables.library.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import co.yml.charts.common.extensions.isNotNull
import com.example.i_prep.common.ConnectionState
import com.example.i_prep.common.NotificationService
import com.example.i_prep.common.connectivityState
import com.example.i_prep.common.decodeAndDecompress
import com.example.i_prep.common.displayLog
import com.example.i_prep.common.displayToast
import com.example.i_prep.common.gson
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.domain.api.share.ShareAPI
import com.example.i_prep.presentation.GlobalEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LImportDialog(
    globalEvent: (GlobalEvent) -> Unit,
    onDismiss: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    var url by rememberSaveable { mutableStateOf("") }
    var isFetching by rememberSaveable { mutableStateOf(false) }

    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available

    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val context = LocalContext.current

    val notification = NotificationService(context)

    AlertDialog(
        onDismissRequest = { },
        confirmButton = { },
        title = {
            Text(
                text = "Import Test",
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
                AnimatedVisibility(visible = isFetching) {
                    CircularProgressIndicator()
                }

                AnimatedVisibility(visible = !isFetching) {
                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        label = { Text(text = "Code") }
                    )
                }

                AnimatedVisibility(visible = !isFetching) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { onDismiss(false) },
                            modifier = modifier.weight(1f)
                        ) {
                            Text(text = "Cancel")
                        }
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    if (!isFetching && isConnected) {
                                        isFetching = true

                                        try {
                                            val text = ShareAPI().getShared(url)

                                            val pTest = gson.fromJson(
                                                decodeAndDecompress(text),
                                                PTest::class.java
                                            )

                                            val newPTest = PTest(
                                                title = pTest.title,
                                                description = pTest.description,
                                                tags = pTest.tags,
                                                questionType = pTest.questionType,
                                                questions = pTest.questions,
                                                totalItems = pTest.questions.size,
                                                language = pTest.language,
                                                image = pTest.image,
                                                dateCreated = pTest.dateCreated,
                                                reference = pTest.reference
                                            )

                                            globalEvent(GlobalEvent.UpsertTest(newPTest))

                                            notification.showNotification(
                                                "${newPTest.title} is imported successfully with ${newPTest.totalItems} questions",
                                                false
                                            )

                                        } catch (e: Exception) {
                                            notification.showNotification("Invalid code", true)
                                        }

                                        onDismiss(false)

                                    } else if (!isConnected) {
                                        notification.showNotification(
                                            "No internet connection",
                                            true
                                        )
                                    }
                                }
                            },
                            modifier = modifier.weight(1f)
                        ) {
                            Text(text = "Submit")
                        }
                    }
                }
            }
        })
}