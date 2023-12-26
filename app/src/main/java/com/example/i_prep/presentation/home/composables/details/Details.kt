package com.example.i_prep.presentation.home.composables.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import co.yml.charts.common.model.Point
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.bumptech.glide.integration.compose.placeholder
import com.example.i_prep.R
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.home.composables.details.components.DFAB
import com.example.i_prep.presentation.home.composables.details.components.DMoreDetail
import com.example.i_prep.presentation.home.composables.details.components.DChart
import com.example.i_prep.presentation.home.composables.details.components.DDescription
import com.example.i_prep.presentation.home.composables.details.components.DModifyDialog
import com.example.i_prep.presentation.home.composables.details.components.DStatistics
import com.example.i_prep.presentation.home.composables.details.components.DTags
import com.example.i_prep.presentation.home.composables.details.components.DTopBar
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalGlideComposeApi::class)
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
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        DModifyDialog(
            pTest = pTest,
            onDismiss = { showDialog = it },
            onModify = {
                globalEvent(GlobalEvent.UpsertTest(it))
            })
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(pTest.image).crossfade(true)
                .build(),
            contentDescription = pTest.title,
            placeholder=  painterResource(R.drawable.ic_launcher_background),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .requiredHeight(250.dp)
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
                .requiredHeight(250.dp),
            content = {}
        )

        Scaffold(
            topBar = { DTopBar(onBack = { onBack() }, onModify = { showDialog = it }) },
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
                    modifier = modifier.padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = modifier
                            .height(150.dp)
                            .width(100.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(pTest.image).crossfade(true)
                                .build(),
                            contentDescription = pTest.title,
                            placeholder=  painterResource(R.drawable.ic_launcher_background),
//                            contentScale = ContentScale.Crop,
                            modifier = modifier.clip(RoundedCornerShape(6.dp))
                        )
                    }

                    Column(
                        modifier = modifier.padding(start = 16.dp)
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
                                Point(
                                    index.toFloat(),
                                    question.shown.toFloat()
                                )
                            },
                            yScale = pTest.questions.maxBy { it.shown }.shown,
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