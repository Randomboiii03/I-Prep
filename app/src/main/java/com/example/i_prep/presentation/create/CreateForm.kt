package com.example.i_prep.presentation.create

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
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.GlobalViewModel
import com.example.i_prep.presentation.create.components.CDropdown
import com.example.i_prep.presentation.create.components.CTopBar
import com.example.i_prep.presentation.create.components.CUploadFile
import com.example.i_prep.presentation.create.model.difficulties
import com.example.i_prep.presentation.create.model.languages
import com.example.i_prep.presentation.create.model.questionTypes
import com.example.i_prep.presentation.create.model.CreateNav
import com.example.i_prep.common.ConnectionState
import com.example.i_prep.common.connectivityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Form(
    mGlobalViewModel: GlobalViewModel,
    globalState: GlobalState,
    globalEvent: (GlobalEvent) -> Unit,
    mCViewModel: CViewModel,
    onEvent: (CEvent) -> Unit,
    navHostController: NavHostController,
    showList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by mCViewModel.state.collectAsState()

    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val notification = NotificationService(LocalContext.current)

    LaunchedEffect(true) {
        if (!globalState.isGenerate) {
            onEvent(CEvent.ResetForm)
        }
    }

    Scaffold(
        topBar = {
            CTopBar(onHelp = {
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

            CDropdown(
                value = state.questionType,
                onValueChange = { onEvent(CEvent.SetForm(state.copy(questionType = it))) },
                list = questionTypes,
                label = "Question Types"
            )

            CUploadFile(state = state, onEvent = onEvent)

            CDropdown(
                value = state.language,
                onValueChange = { onEvent(CEvent.SetForm(state.copy(language = it))) },
                list = languages,
                label = "Language"
            )

            CDropdown(
                value = state.difficulty,
                onValueChange = { onEvent(CEvent.SetForm(state.copy(difficulty = it))) },
                list = difficulties,
                label = "Difficulty"
            )

//            Divider(modifier = modifier.padding(horizontal = 16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    onClick = { onEvent(CEvent.ResetForm) },
                    enabled = mCViewModel.onReset(),
                    modifier = modifier.width(107.dp)
                ) {
                    Text(text = "Reset")
                }

                Button(
                    onClick = {
                        globalEvent(GlobalEvent.Generate(true))

                        coroutineScope.launch {
                            mGlobalViewModel.runAPI(state, notification)
                        }
                    },
                    enabled = mCViewModel.onGenerate() && isConnected && !globalState.isGenerate,
                ) {
                    Text(text = "Generate")
                }
            }
        }
    }
}