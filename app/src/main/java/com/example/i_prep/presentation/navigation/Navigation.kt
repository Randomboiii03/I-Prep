package com.example.i_prep.presentation.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalViewModel
import com.example.i_prep.presentation.create.CreateNavHost
import com.example.i_prep.presentation.history.HistoryNavHost
import com.example.i_prep.presentation.home.HomeNavHost
import com.example.i_prep.presentation.more.MoreNavHost
import com.example.i_prep.presentation.navigation.components.BottomNavAnimation
import com.example.i_prep.presentation.navigation.model.BottomNav
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.randomboiii.i_prep.presentation.use_case.ConnectionState
import com.randomboiii.i_prep.presentation.use_case.connectivityState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalCoroutinesApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(mGlobalViewModel: GlobalViewModel) {
    val rootNavController = rememberNavController()
    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()

    val state by mGlobalViewModel.state.collectAsState()

    val postNotificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available
    val context = LocalContext.current

    LaunchedEffect(true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }

        if (isConnected) {
            mGlobalViewModel.onEvent(GlobalEvent.CheckUpdate(context, false))
        }
    }

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
        NavHost(
            navController = rootNavController,
            startDestination = if (state.pTestList.filter { it.isAvailable }.isEmpty()) BottomNav.Create.title else BottomNav.Home.title
        ) {
            composable(
                route = BottomNav.Home.title,
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
                route = BottomNav.Create.title,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                }
            ) {
                CreateNavHost(
                    globalEvent = mGlobalViewModel::onEvent,
                    navHostController = rootNavController
                )
            }

            composable(
                route = BottomNav.History.title,
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
                route = BottomNav.More.title,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                }
            ) {
                MoreNavHost(globalState = state, globalEvent = mGlobalViewModel::onEvent)
            }
        }
    }
}