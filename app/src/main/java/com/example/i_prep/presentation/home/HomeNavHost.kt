package com.example.i_prep.presentation.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.home.composables.details.Details
import com.example.i_prep.presentation.home.composables.library.Library
import com.example.i_prep.presentation.home.composables.result.Result
import com.example.i_prep.presentation.home.composables.test.TEvent
import com.example.i_prep.presentation.home.composables.test.TViewModel
import com.example.i_prep.presentation.home.composables.test.mc.TestMC
import com.example.i_prep.presentation.home.composables.test.sa.TestSA
import com.example.i_prep.presentation.home.model.HomeNav

@Composable
fun HomeNavHost(
    globalState: GlobalState,
    globalEvent: (GlobalEvent) -> Unit
) {
    val homeNavHostController = rememberNavController()

    val mTViewModel = viewModel<TViewModel>()
    val onEvent = mTViewModel::onEvent

    NavHost(navController = homeNavHostController, startDestination = HomeNav.Library.title) {
        composable(
            route = HomeNav.Library.title,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            Library(
                globalState = globalState,
                globalEvent = globalEvent,
                navHostController = homeNavHostController
            )
        }

        composable(
            route = HomeNav.Details.title,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            Details(
                globalState = globalState,
                globalEvent = globalEvent,
                onBack = { homeNavHostController.popBackStack() },
                takeTest = {
                    onEvent(TEvent.InitializeTest(it, homeNavHostController))
                    homeNavHostController.navigate("Test")
                }
            )
        }

        composable(
            route = HomeNav.Test.title)
        {
            when (globalState.pTest.questionType != "sa") {
                true -> TestMC(
                    mTViewModel = mTViewModel,
                    onEvent = { onEvent(it) },
                    navHostController = homeNavHostController
                )

                false -> TestSA(
                    mTViewModel = mTViewModel,
                    onEvent = { onEvent(it) },
                    navHostController = homeNavHostController
                )
            }
        }

        composable(
            route = "Result",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            Result(
                globalState = globalState,
                globalEvent = globalEvent,
                mTViewModel = mTViewModel,
                onEvent = mTViewModel::onEvent,
                navHostController = homeNavHostController
            )
        }
    }
}