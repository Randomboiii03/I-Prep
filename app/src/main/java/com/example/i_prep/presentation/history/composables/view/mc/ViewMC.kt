package com.example.i_prep.presentation.history.composables.view.mc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.i_prep.presentation.history.composables.view.VEvent
import com.example.i_prep.presentation.history.composables.view.VState
import com.example.i_prep.presentation.history.composables.view.VVIewModel
import com.example.i_prep.presentation.history.composables.view.components.VTopBar
import com.example.i_prep.presentation.history.model.HistoryNav
import com.example.i_prep.presentation.home.composables.test.components.TBottomBar
import com.example.i_prep.presentation.home.composables.test.components.TQuestion
import com.example.i_prep.presentation.home.composables.test.components.TTopBar

@Composable
fun ViewMC(
    mVVIewModel: VVIewModel,
    onEvent: (VEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val state by mVVIewModel.state.collectAsState()

    Scaffold(
        topBar = {
            VTopBar(
                onBack = { navHostController.popBackStack() },
                title = state.pTest.title
            )
        },
        bottomBar = {
            TBottomBar(
                onNext = {
                    if (state.currentQIndex + 1 == state.pTest.itemSet) {
                        navHostController.navigate(HistoryNav.Result.title) { popUpTo(HistoryNav.Archive.title) }
                    } else onEvent(VEvent.NextQuestion)
                },
                onPrevious = { onEvent(VEvent.PreviousQuestion) },
                isNext = !state.isLoading,
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
                    TContent(state = state)
                }
            }
        }
    }
}

@Composable
private fun TContent(state: VState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TCount(currentIndex = state.currentQIndex, totalItems = state.tHistory.questionsTaken)

        TQuestion(question = state.tHistory.questions[state.currentQIndex].question)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.tHistory.questions[state.currentQIndex].choices) { item ->
                TChoices(
                    answer = state.tHistory.selectedAnswer[state.currentQIndex],
                    choice = item,
                    correctAnswer = state.tHistory.questions[state.currentQIndex].answer
                )
            }
        }
    }
}

@Composable
fun TCount(
    currentIndex: Int,
    totalItems: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Question ${currentIndex + 1} of $totalItems",
            style = MaterialTheme.typography.titleMedium
        )

        LinearProgressIndicator(
            progress = currentIndex.toFloat() / (totalItems - 1).toFloat(),
            strokeCap = StrokeCap.Round,
            modifier = modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp)
        )
    }
}

@Composable
private fun TChoices(
    answer: String,
    choice: String,
    correctAnswer: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (answer == choice) {
                true -> {
                    when (correctAnswer == answer) {
                        true -> MaterialTheme.colorScheme.primary
                        false -> MaterialTheme.colorScheme.errorContainer
                    }
                }

                false -> MaterialTheme.colorScheme.primaryContainer
            }
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = choice,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = when (answer == choice) {
                    true -> {
                        when (correctAnswer == answer) {
                            true -> MaterialTheme.colorScheme.onPrimary
                            false -> MaterialTheme.colorScheme.onErrorContainer
                        }
                    }

                    false -> MaterialTheme.colorScheme.onPrimaryContainer
                },
                modifier = modifier
                    .fillMaxWidth()
            )
        }
    }
}