package com.example.i_prep.presentation.create.composables.generate

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.i_prep.common.NotificationService
import com.example.i_prep.common.emptyPTest
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.create.CViewModel
import com.example.i_prep.presentation.create.model.CreateNav

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Generate(
    globalEvent: (GlobalEvent) -> Unit,
    navHostController: NavHostController,
    mCViewModel: CViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(false))
    }

    val mGViewModel = viewModel<GViewModel>()
    val onEvent = mGViewModel::onEvent

    val cState by mCViewModel.state.collectAsState()
    val state by mGViewModel.state.collectAsState()

    val context = LocalContext.current
    val notification = NotificationService(context)

    LaunchedEffect(true) {
        if (state.status.isEmpty()) {
            onEvent(GEvent.GenerateTest(cState))
        }
    }

    if (state.showNotification) {
        notification.showNotification(state.message, state.isError)

        if (!state.isError) {
            globalEvent(GlobalEvent.UpsertTest(state.pTest))
        }

        onEvent(GEvent.ShowNotification(false, "", false))

        navHostController.navigate(CreateNav.Form.title){
            popUpTo(CreateNav.Form.title)
        }
    }

    Scaffold {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(text = state.status)
            }
        }
    }
}