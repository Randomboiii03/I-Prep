package com.example.i_prep.presentation.home.composables.test.mc

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.home.composables.test.TEvent
import com.example.i_prep.presentation.home.composables.test.TState
import com.example.i_prep.presentation.home.composables.test.TViewModel
import com.example.i_prep.presentation.home.composables.test.components.TBottomBar
import com.example.i_prep.presentation.home.composables.test.components.TQuestion
import com.example.i_prep.presentation.home.composables.test.components.TTimer
import com.example.i_prep.presentation.home.composables.test.components.TTopBar
import com.example.i_prep.presentation.home.model.HomeNav

@Composable
fun TestMC(
    mTViewModel: TViewModel,
    onEvent: (TEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val state by mTViewModel.state.collectAsState()

    var showDialog by rememberSaveable { mutableStateOf(false) }

    BackHandler {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    mTViewModel.stopTimer()
                    showDialog = false
                    navHostController.popBackStack()}
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
            },
            icon = { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back") },
            title = { Text(text = "Go Back") },
            text = { Text(text = "Would you like to go back?") },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = { TTopBar(title = state.pTest.title) },
        bottomBar = {
            TBottomBar(
                onNext = {
                    if (state.currentQIndex + 1 == state.pTest.itemSet) {
                        showDialog = false
                        onEvent(TEvent.CheckResult(navHostController))
                    } else onEvent(TEvent.NextQuestion)
                },
                onPrevious = { onEvent(TEvent.PreviousQuestion) },
                isNext = !state.isLoading && state.answers[state.currentQIndex] != "",
                isPrevious = state.currentQIndex > 0,
                isDone = state.currentQIndex + 1 == state.pTest.itemSet
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state.isLoading) {
                true -> {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                false -> {
                    TContent(state = state, onEvent = { onEvent(it) })
                }
            }
        }
    }
}

@Composable
private fun TContent(state: TState, onEvent: (TEvent) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TTimer(
            time = state.time,
            currentIndex = state.currentQIndex,
            totalItems = state.pTest.itemSet,
            isTimed = state.pTest.isTimed
        )

        TQuestion(question = state.questions[state.currentQIndex].question)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.questions[state.currentQIndex].choices) { item ->
                TChoices(
                    answer = state.answers[state.currentQIndex],
                    choice = item,
                    onClickItem = { onEvent(TEvent.InsertAnswer(it)) }
                )
            }
        }
    }
}

@Composable
private fun TChoices(
    answer: String,
    choice: String,
    onClickItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (answer == choice) {
                true -> MaterialTheme.colorScheme.primary
                false -> MaterialTheme.colorScheme.primaryContainer
            }
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickItem(choice) }
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = choice,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }
    }
}