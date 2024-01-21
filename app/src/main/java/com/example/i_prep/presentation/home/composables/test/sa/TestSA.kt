package com.example.i_prep.presentation.home.composables.test.sa

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.i_prep.presentation.home.composables.test.TEvent
import com.example.i_prep.presentation.home.composables.test.TViewModel

@Composable
fun TestSA(
    mTViewModel: TViewModel,
    onEvent: (TEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box {
        Text(text = "Test SA")
    }
}