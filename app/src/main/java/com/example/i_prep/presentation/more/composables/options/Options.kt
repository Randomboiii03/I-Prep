package com.example.i_prep.presentation.more.composables.options

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.i_prep.R
import com.example.i_prep.common.gson
import com.example.i_prep.domain.app_updater.AppUpdater
import com.example.i_prep.domain.app_updater.model.UpdateChangeLog
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.more.composables.options.components.OItem
import com.example.i_prep.presentation.more.model.MoreNav
import com.example.i_prep.presentation.more.model.moreNav
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Options(
    globalEvent: (GlobalEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(true))
    }

    val scope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier.padding(vertical = 75.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = modifier.size(125.dp)
                )
            }

            Divider()

            LazyColumn {
                items(moreNav) { item ->
                    OItem(
                        title = item.title,
                        icon = item.icon,
                        onClickItem = {
                            when (item.title != MoreNav.CheckUpdate.title) {
                                true -> navHostController.navigate(item.title)
                                false -> scope.launch(Dispatchers.IO) {
                                    val updateChangeLog = AppUpdater().checkUpdates()

                                    if (updateChangeLog != null) {
                                        Log.v("TAG", updateChangeLog.toString())
                                    }
                                }
                            }
                        })
                }
            }
        }
    }
}