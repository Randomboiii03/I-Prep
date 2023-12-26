package com.example.i_prep.presentation.home.composables.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.i_prep.common.dummyQuestions
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.home.composables.library.components.HBottomSheet
import com.example.i_prep.presentation.home.composables.library.components.HItem
import com.example.i_prep.presentation.home.composables.library.components.HTopBar
import com.example.i_prep.presentation.home.model.HomeNav

@Composable
fun Library(
    globalState: GlobalState,
    globalEvent: (GlobalEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(true) {
//        for (i in 1..5) {
//            globalEvent(
//                GlobalEvent.UpsertTest(
//                    PTest(
//                        title = "Title $i",
//                        description = "Description $i",
//                        tags = listOf("Tag 01", "Tag 02", "Tag 03", "Tag 04"),
//                        questionType = "mcq",
//                        questions = dummyQuestions(50),
//                        totalItems = 50,
//                        language = "English",
//                        reference = "Unknown.pdf",
//                        image = "https://picsum.photos/400/600",
//                        dateCreated = System.currentTimeMillis(),
//                    )
//                )
//            )
//        }
        globalEvent(GlobalEvent.ShowBottomNav)
    }

    val mLViewModel = viewModel<LViewModel>()
    val state by mLViewModel.state.collectAsState()
    val onEvent = mLViewModel::onEvent

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
                })
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.showFilter) {
                HBottomSheet(onDismiss = { onEvent(LEvent.Filter) })
            }

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
                    Column(
                        modifier = modifier.fillMaxSize()
                    ) {
                        LazyColumn {
                            items(
                                items = if (state.isSearch) globalState.pTestListFiltered else globalState.pTestList,
                                key = { it.testId }
                            ) { item: PTest ->
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