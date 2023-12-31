package com.example.i_prep

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.IPrepTheme
import com.example.i_prep.presentation.GlobalViewModel
import com.example.i_prep.presentation.navigation.Navigation
import com.example.i_prep.presentation.splash.SplashViewModel
import com.example.i_prep.presentation.welcome.Welcome
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )

        super.onCreate(savedInstanceState)
        setContent {
            IPrepTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val screen by splashViewModel.startDestination
                    val mainNavHostController = rememberNavController()

                    val mGlobalViewModel = viewModel<GlobalViewModel>()
                    Log.v("TAG", screen)
                    NavHost(navController = mainNavHostController, startDestination = screen) {

                        composable(route = "Blank") {
                            Box(Modifier.fillMaxSize())
                        }

                        composable(route = "Welcome") {
                            Welcome(navHostController = mainNavHostController)
                        }
                        
                        composable(route = "Main") {
                            Navigation(mGlobalViewModel = mGlobalViewModel)
                        }
                    }
                }
            }
        }
    }
}