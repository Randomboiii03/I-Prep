package com.example.i_prep.presentation.create.composables.form

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.i_prep.common.NotificationService
import com.example.i_prep.presentation.create.CEvent
import com.example.i_prep.presentation.create.CViewModel
import com.example.i_prep.presentation.create.composables.form.components.FDropdown
import com.example.i_prep.presentation.create.composables.form.components.FTopBar
import com.example.i_prep.presentation.create.composables.form.components.FUploadFile
import com.example.i_prep.presentation.create.composables.form.model.difficulties
import com.example.i_prep.presentation.create.composables.form.model.languages
import com.example.i_prep.presentation.create.composables.form.model.questionTypes
import com.example.i_prep.presentation.create.model.CreateNav
import com.randomboiii.i_prep.presentation.use_case.ConnectionState
import com.randomboiii.i_prep.presentation.use_case.connectivityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Form(
    mCViewModel: CViewModel,
    onEvent: (CEvent) -> Unit,
    navHostController: NavHostController,
    showList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by mCViewModel.state.collectAsState()

    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val notification = NotificationService(LocalContext.current)

    LaunchedEffect(true) {
        if (state.isGenerate) {
            onEvent(CEvent.Generate(false))
            onEvent(CEvent.Reset)
            showList()
        }
    }

    Scaffold(
        topBar = {
            FTopBar(onHelp = {
                navHostController.navigate(CreateNav.Help.title) {
                    popUpTo(CreateNav.Form.title)
                }
            })
        },
        floatingActionButton = {
            if (!isConnected) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "No internet connection available",
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier
                    .navigationBarsPadding()
                    .padding(bottom = 32.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "Create Test", style = MaterialTheme.typography.headlineSmall)

            HorizontalDivider(modifier = modifier.padding(horizontal = 16.dp))

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
                        onEvent(CEvent.Generate(true))
                        notification.showNotification("Generation start please wait...", false)

                        coroutineScope.launch {
                            try {
                                mCViewModel.runAPI()
                            } catch (e: Exception) {
                                notification.showNotification("Error: $e", false)
                            }
                        }
                    },
                    enabled = mCViewModel.onGenerate() && isConnected && !state.isGenerate,
                ) {
                    Text(text = "Generate")
                }
            }
        }
    }
}