package com.example.i_prep.presentation.create

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.create.composables.form.Form
import com.example.i_prep.presentation.create.model.CreateNav
import com.example.i_prep.presentation.more.composables.help.Help
import com.example.i_prep.presentation.navigation.model.BottomNav

@Composable
fun CreateNavHost(globalEvent: (GlobalEvent) -> Unit, navHostController: NavHostController) {
    val createNavHostController = rememberNavController()

    val mCViewModel = viewModel<CViewModel>()

    NavHost(navController = createNavHostController, startDestination = CreateNav.Form.title) {
        composable(
            route = CreateNav.Form.title,
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            LaunchedEffect(true) { globalEvent(GlobalEvent.ShowBottomNav(true)) }

            Form(
                mCViewModel = mCViewModel,
                onEvent = mCViewModel::onEvent,
                navHostController = createNavHostController,
                showList = { navHostController.navigate(BottomNav.Home.title) })
        }

        composable(
            route = CreateNav.Help.title,
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
            Help(globalEvent = globalEvent, onBack = { createNavHostController.popBackStack() })
        }
    }
}