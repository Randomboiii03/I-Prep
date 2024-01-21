package com.example.i_prep.presentation.more

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.more.composables.about.AboutNavHost
import com.example.i_prep.presentation.more.composables.help.Help
import com.example.i_prep.presentation.more.composables.options.Options
import com.example.i_prep.presentation.more.composables.statistics.Statistics
import com.example.i_prep.presentation.more.composables.uploaded.UploadedFile
import com.example.i_prep.presentation.more.model.MoreNav

@Composable
fun MoreNavHost(globalState: GlobalState, globalEvent: (GlobalEvent) -> Unit) {
    val moreNavHostController = rememberNavController()

    NavHost(navController = moreNavHostController, startDestination = MoreNav.Options.title) {
        composable(route = MoreNav.Options.title) {
            Options(globalEvent = globalEvent, navHostController = moreNavHostController)
        }

        composable(
            route = MoreNav.Statistics.title,
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
            Statistics(globalState = globalState, globalEvent = globalEvent, onBack = { moreNavHostController.popBackStack() })
        }

        composable(
            route = MoreNav.UploadedFiles.title,
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
            UploadedFile(globalState = globalState, globalEvent = globalEvent, onBack = { moreNavHostController.popBackStack() })
        }

        composable(
            route = MoreNav.Help.title,
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
            Help(globalEvent = globalEvent, onBack = { moreNavHostController.popBackStack() })
        }

        composable(
            route = MoreNav.About.title,
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
            AboutNavHost(globalEvent = globalEvent, onBack = { moreNavHostController.popBackStack() })
        }
    }
}