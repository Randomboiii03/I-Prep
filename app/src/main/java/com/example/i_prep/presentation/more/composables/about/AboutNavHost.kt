package com.example.i_prep.presentation.more.composables.about

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.more.composables.about.composables.FAQ
import com.example.i_prep.presentation.more.composables.about.composables.PrivacyPolicy
import com.example.i_prep.presentation.more.composables.about.model.AboutNav

@Composable
fun AboutNavHost(globalEvent: (GlobalEvent) -> Unit, onBack: () -> Unit) {

    val aboutNavHostController = rememberNavController()

    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(false))
    }

    NavHost(navController = aboutNavHostController, startDestination = AboutNav.About.title) {
        composable(route = AboutNav.About.title) {
            About(onBack = { onBack() }, globalEvent = globalEvent, navHostController = aboutNavHostController)
        }

        composable(
            route = AboutNav.PrivacyPolicy.title,
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
            PrivacyPolicy(onBack = { aboutNavHostController.popBackStack() })
        }

        composable(
            route = AboutNav.FAQ.title,
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
            FAQ(onBack = { aboutNavHostController.popBackStack() })
        }
    }
}