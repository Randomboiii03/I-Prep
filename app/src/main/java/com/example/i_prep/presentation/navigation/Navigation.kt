package com.example.i_prep.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.presentation.GlobalViewModel
import com.example.i_prep.presentation.history.HistoryNavHost
import com.example.i_prep.presentation.home.HomeNavHost
import com.example.i_prep.presentation.navigation.components.BottomNavAnimation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(mGlobalViewModel: GlobalViewModel) {
    val rootNavController = rememberNavController()
    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()

    val state by mGlobalViewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = state.showBottomNav,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut()
            ) {
                BottomNavAnimation(
                    rootNavController = rootNavController,
                    navBackStackEntry = navBackStackEntry
                )
            }
        }
    ) {
        NavHost(navController = rootNavController, startDestination = "Home") {
            composable(
                route = "Home",
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
                HomeNavHost(globalState = state, globalEvent = mGlobalViewModel::onEvent)
            }

            composable(
                route = "Create",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Create")
                }
            }

            composable(
                route = "History",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                }
            ) {
                HistoryNavHost(globalState = state, globalEvent = mGlobalViewModel::onEvent)
            }

            composable(
                route = "More",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "More")
                }
            }
        }
    }
}