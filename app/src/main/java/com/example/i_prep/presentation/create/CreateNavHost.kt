package com.example.i_prep.presentation.create

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.create.composables.form.Form
import com.example.i_prep.presentation.create.composables.generate.Generate
import com.example.i_prep.presentation.create.model.CreateNav

@Composable
fun CreateNavHost(    globalEvent: (GlobalEvent) -> Unit) {
    val createNavHostController = rememberNavController()

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
            Form(globalEvent = globalEvent, navHostController = createNavHostController)
        }

        composable(
            route = CreateNav.Generate.title,
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
            Generate()
        }
    }
}