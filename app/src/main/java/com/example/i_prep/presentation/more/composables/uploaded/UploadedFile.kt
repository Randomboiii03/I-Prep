package com.example.i_prep.presentation.more.composables.uploaded

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.more.components.MTopBar
import com.example.i_prep.presentation.more.composables.uploaded.components.UITem
import java.io.File

@Composable
fun UploadedFile(
    globalEvent: (GlobalEvent) -> Unit,
    globalState: GlobalState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(false))
    }

    Scaffold(
        topBar = { MTopBar(onBack = { onBack() }, title = "Uploaded File") }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val list = globalState.pTestList.distinctBy { it.reference }.reversed()

            when (list.isEmpty()) {
                true -> {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .navigationBarsPadding(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No reference file available yet",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                false -> {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .padding(bottom = 20.dp)
                    ) {
                        LazyColumn {
                            items(items = list, key = { it.testId }) { item: PTest ->
                                UITem(referenceFile = File(item.reference))
                            }
                        }
                    }
                }
            }
        }
    }
}