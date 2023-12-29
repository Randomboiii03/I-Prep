package com.example.i_prep.presentation.create.composables.form

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.extensions.isNotNull
import com.example.i_prep.domain.api.IPrepAPI
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.create.CEvent
import com.example.i_prep.presentation.create.CViewModel
import com.example.i_prep.presentation.create.composables.form.components.FDropdown
import com.example.i_prep.presentation.create.composables.form.components.FTopBar
import com.example.i_prep.presentation.create.composables.form.components.FUploadFile
import com.example.i_prep.presentation.create.composables.form.model.difficulties
import com.example.i_prep.presentation.create.composables.form.model.languages
import com.example.i_prep.presentation.create.composables.form.model.questionTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun Form(
    globalEvent: (GlobalEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val mCViewModel = viewModel<CViewModel>()
    val state by mCViewModel.state.collectAsState()
    val onEvent = mCViewModel::onEvent

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { FTopBar(onReset = {}) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "Create Test", style = MaterialTheme.typography.titleLarge)

            Divider(modifier = modifier.padding(horizontal = 16.dp))

            FDropdown(
                value = state.questionType,
                onValueChange = { onEvent(CEvent.SetQuestionType(it)) },
                list = questionTypes,
                label = "Question Types"
            )

            FUploadFile(fileName = state.fileName, onEvent = onEvent)

            FDropdown(
                value = state.language,
                onValueChange = { onEvent(CEvent.SetLanguage(it)) },
                list = languages,
                label = "Language"
            )

            FDropdown(
                value = state.difficulty,
                onValueChange = { onEvent(CEvent.SetDifficulty(it)) },
                list = difficulties,
                label = "Difficulty"
            )

//            Divider(modifier = modifier.padding(horizontal = 16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    onClick = { onEvent(CEvent.Reset) },
                    enabled = mCViewModel.onReset(),
                    modifier = modifier.width(107.dp)
                ) {
                    Text(text = "Reset")
                }

                Button(
                    onClick = {
//                        navHostController.navigate(CreateNav.Generate.title) {
//                            popUpTo(
//                                CreateNav.Form.title
//                            )
//                        }

                        scope.launch(Dispatchers.IO) {
                            val api =
                                IPrepAPI("__ssid=4ed0f3c3ba0d300234022b3c69a88ed; intercom-device-id-lupk8zyo=4d8c53c7-e78f-4367-ae63-23cb7adc736a; sessionKey=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; _gcl_au=1.1.640486467.1703215684; activitySessionId=64fc7436-3f1e-46e8-9b22-25349f72cbc0; __cf_bm=n450Lh8UTGJ200ERD.PcQ4no_gNMAtb8G0FAvN.B6Us-1703822242-1-AckegxkVdsdA2Z8hqioirkJcvImcYEaGbYoIzzvZgCsMrP2miN50Rp8p1T5QzNkJgyz+QVQttWiwBHJw49vC64U=; cf_clearance=ilPFMi5RiQXMGyJvSFqRD2FsINwE9YdX29alHXBwkKU-1703822245-0-2-12cc6e78.222f4e72.45cd3198-0.2.1703822245; intercom-session-lupk8zyo=VEZKdHpDRU16VUJLL2hIdjZzeFcrRGZUdWFxc1NtZUZwK2ovVm5UcGYra2NPR0JSYXZieEFXU2tPZkQ3SjA5TS0tN0RDb2hSWWlCMmEyMEtsTWcyRUFFQT09--31ec7e1bc6124f5af4624d9e5b001ad2b7bc8ec7")
                            delay(3000)
//                            val attachment = api.uploadAttachment(File(state.filePath))
//
//                            if (attachment.isNotNull()) {
//                                Log.v("TAG", attachment.toString())
//                                val conversationId = api.createNewChat().toString()
//                                Log.v("TAG", "ConversationId: $conversationId")
//                                val message = api.sendMessage(
//                                    conversationId = conversationId,
//                                    prompt = "Summarize the uploaded document, but it must always start it I'm",
//                                    attachments = listOf(attachment)
//                                )
//                                Log.v("TAG", message.toString())
//                            }
                            api.deleteConversation("127f8743-7348-4336-8e05-45adda16babb")
                        }

                    },
//                    mCViewModel.onGenerate()
                    enabled = true,
                ) {
                    Text(text = "Generate")
                }
            }
        }
    }
}