package com.example.i_prep.presentation.more.composables.help

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.i_prep.common.about1
import com.example.i_prep.common.about2_1
import com.example.i_prep.common.about2_2
import com.example.i_prep.common.about3
import com.example.i_prep.common.about4
import com.example.i_prep.common.claudePrivPol
import com.example.i_prep.common.githubRepo
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.more.composables.components.MTopBar

@Composable
fun Help(globalEvent: (GlobalEvent) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(false))
    }

    Scaffold(
        topBar = { MTopBar(onBack = { onBack() }, title = "Help") }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

        }
    }
}