package com.example.i_prep.presentation.more.composables.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.Point
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.home.composables.details.formatDate
import com.example.i_prep.presentation.more.components.MTopBar
import com.example.i_prep.presentation.more.composables.statistics.components.SHistory
import com.example.i_prep.presentation.more.composables.statistics.components.SQuestion
import com.example.i_prep.presentation.more.composables.statistics.components.STag
import com.example.i_prep.presentation.more.composables.statistics.components.STest

@Composable
fun Statistics(globalState: GlobalState,  globalEvent: (GlobalEvent) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(false))
    }

    Scaffold(
        topBar = { MTopBar(onBack = { onBack() }, title = "Statistics") }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            STest(globalState = globalState)

            SHistory(globalState = globalState)
            
            if (globalState.pTestList.isNotEmpty()) {
                SQuestion(globalState = globalState)
            }
            
            STag(globalState = globalState)
        }
    }
}