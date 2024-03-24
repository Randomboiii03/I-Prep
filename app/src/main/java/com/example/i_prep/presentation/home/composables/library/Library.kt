package com.example.i_prep.presentation.home.composables.library

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.i_prep.common.NotificationService
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.home.composables.library.components.HItem
import com.example.i_prep.presentation.home.composables.library.components.HTopBar
import com.example.i_prep.presentation.home.composables.library.components.LImportDialog
import com.example.i_prep.presentation.home.model.HomeNav
import java.io.IOException

@Composable
fun Library(
    globalState: GlobalState,
    globalEvent: (GlobalEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(true))
    }

    val mLViewModel = viewModel<LViewModel>()
    val state by mLViewModel.state.collectAsState()
    val onEvent = mLViewModel::onEvent

    val context = LocalContext.current
    val notification = NotificationService(context)

    var showImportDialog by rememberSaveable { mutableStateOf(false) }

    if (showImportDialog) {
        LImportDialog(globalEvent = globalEvent, onDismiss = { showImportDialog = it })
    }

    Scaffold(
        topBar = {
            HTopBar(
                isSearch = state.isSearch,
                showSearch = {
                    onEvent(LEvent.Search)
                    globalEvent(GlobalEvent.SearchTest(""))
                },
                showFilter = { onEvent(LEvent.Filter) },
                searchText = state.searchText,
                onSearch = {
                    onEvent(LEvent.onSearch(it))
                    globalEvent(GlobalEvent.SearchTest(it))
                },
                importTest = { showImportDialog = it })
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (globalState.isLoading) {
                true -> {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                false -> {
                    val list =
                        (if (state.isSearch) globalState.pTestListFiltered else globalState.pTestList)
                            .filter { it.isAvailable }
                            .reversed()

                    when (list.isEmpty()) {
                        true -> {
                            Box(
                                modifier = modifier
                                    .fillMaxSize()
                                    .navigationBarsPadding(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (state.isSearch) "No test result" else "No test available yet",
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
                                        HItem(
                                            pTest = item,
                                            onClickItem = {
                                                globalEvent(GlobalEvent.GetTest(it))
                                                navHostController.navigate(HomeNav.Details.title) {
                                                    popUpTo(HomeNav.Library.title)
                                                }
                                            })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun extractTextFromUri(context: Context, uri: Uri): String? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri) ?: return null

    return try {
        inputStream.bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        // Handle potential exceptions during reading
        Log.e("FileReadingError", "Error reading text from file: $uri", e)
        null
    }
}
