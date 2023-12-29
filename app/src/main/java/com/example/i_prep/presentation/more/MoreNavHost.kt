package com.example.i_prep.presentation.more

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.i_prep.presentation.more.composables.options.Options
import com.example.i_prep.presentation.more.model.MoreNav

@Composable
fun MoreNavHost() {
    val moreNavHostController = rememberNavController()

    NavHost(navController = moreNavHostController, startDestination = MoreNav.Options.title) {
        composable(route = MoreNav.Options.title) {
            Options()
        }

        composable(route = MoreNav.Statistics.title) {

        }

        composable(route = MoreNav.UploadedFiles.title) {

        }

        composable(route = MoreNav.Help.title) {

        }

        composable(route = MoreNav.About.title) {

        }
    }
}