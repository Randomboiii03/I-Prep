package com.example.i_prep.presentation.home.composables.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.Point
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.i_prep.R
import com.example.i_prep.common.compressAndEncode
import com.example.i_prep.common.gson
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.home.composables.details.components.DChart
import com.example.i_prep.presentation.home.composables.details.components.DDescription
import com.example.i_prep.presentation.home.composables.details.components.DFAB
import com.example.i_prep.presentation.home.composables.details.components.DModifyDialog
import com.example.i_prep.presentation.home.composables.details.components.DMoreDetail
import com.example.i_prep.presentation.home.composables.details.components.DShareDialog
import com.example.i_prep.presentation.home.composables.details.components.DStatistics
import com.example.i_prep.presentation.home.composables.details.components.DTags
import com.example.i_prep.presentation.home.composables.details.components.DTopBar
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun Details(
    globalState: GlobalState,
    globalEvent: (GlobalEvent) -> Unit,
    onBack: () -> Unit,
    takeTest: (PTest) -> Unit,
    modifier: Modifier = Modifier
) {
    var pTest = globalState.pTest
    val historyList = globalState.tHistoryList

    var changeChart by rememberSaveable { mutableStateOf(true) }
    var showModifyDialog by rememberSaveable { mutableStateOf(false) }
    var showShareDialog by rememberSaveable { mutableStateOf(false) }

    var rowHeight by rememberSaveable { mutableStateOf(250) }

    val context = LocalContext.current

    if (showModifyDialog) {
        DModifyDialog(
            pTest = pTest,
            onDismiss = { showModifyDialog = it },
            onModify = {
                globalEvent(GlobalEvent.UpsertTest(it))
            })
    }

    if (showShareDialog) {
        DShareDialog(
            text = compressAndEncode(gson.toJson(pTest.copy(questions = pTest.questions.map { it.copy(correct = 0, shown = 0) }))),
            onDismiss = { showShareDialog = it })
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(pTest.image).crossfade(true)
                .build(),
            contentDescription = pTest.title,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .requiredHeight(rowHeight.dp)
                .alpha(.5f)
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .requiredHeight(rowHeight.dp),
            content = {}
        )

        Scaffold(
            topBar = {
                DTopBar(
                    onBack = { onBack() },
                    onModify = { showModifyDialog = it },
                    onDelete = {
                        globalEvent(GlobalEvent.DeleteTest(pTest))
                        onBack()
                    },
                    onShare = { showShareDialog = it })
            },
            floatingActionButton = { DFAB(onClickFAB = { takeTest(globalState.pTest) }) },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .onGloballyPositioned {
                            rowHeight = it.size.height - 185
                        }
                ) {
                    Column(
                        modifier = modifier.size(height = 150.dp, width = 100.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(pTest.image)
                                .crossfade(true)
                                .build(),
                            contentDescription = pTest.title,
                            placeholder = painterResource(R.drawable.ic_launcher_background),
                            modifier = modifier.clip(RoundedCornerShape(6.dp))
                        )
                    }

                    Column(
                        modifier = modifier.padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(text = pTest.title, style = MaterialTheme.typography.titleLarge)

                        DMoreDetail(
                            text = "${formatDate(pTest.dateCreated)}",
                            icon = Icons.Outlined.CalendarMonth
                        )

                        DMoreDetail(text = pTest.reference, icon = Icons.Outlined.AttachFile)
                        DMoreDetail(text = pTest.language, icon = Icons.Outlined.Language)
                    }
                }

                DDescription(desc = pTest.description)

                DTags(tags = pTest.tags)

                DStatistics(
                    viewedQuestion = pTest.questions.count { it.shown > 0 },
                    totalQuestion = pTest.totalItems,
                    correctAnswered = pTest.questions.count { it.correct > 0 },
                    testTaken = historyList.count { it.testId == pTest.testId },
                    onChange = changeChart,
                    onChangeChart = { changeChart = it }
                )

                Row {
                    AnimatedVisibility(visible = changeChart) {
                        DChart(
                            pointData = pTest.questions.mapIndexed { index, question ->
                                Point(index.toFloat(), question.shown.toFloat())
                            },
                            yScale = pTest.questions.map { it.shown }.max(),
                            label = "Viewed Question"
                        )
                    }

                    AnimatedVisibility(visible = !changeChart) {
                        DChart(
                            pointData = pTest.questions.mapIndexed { index, question ->
                                Point(
                                    index.toFloat(),
                                    question.correct.toFloat()
                                )
                            },
                            yScale = pTest.questions.maxBy { it.correct }.correct,
                            label = "Answered Correct"
                        )
                    }
                }
            }
        }
    }
}

fun formatDate(dateCreated: Long): String? {
    val instant = Instant.ofEpochMilli(dateCreated)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    return formatter.format(localDateTime)
}