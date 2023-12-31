package com.example.i_prep.presentation.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.i_prep.R
import com.example.i_prep.presentation.welcome.model.OnBoardingPage
import com.example.i_prep.presentation.welcome.model.onBoardingPage
import com.google.accompanist.pager.*

@Composable
fun Welcome(
    navHostController: NavHostController,
    mWViewModel: WViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()

    Column(modifier = modifier.fillMaxSize().navigationBarsPadding()) {
        HorizontalPager(
            modifier = modifier.weight(11f),
            count = 3,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            Pager(onBoardingPage = onBoardingPage[position])
        }

        HorizontalPagerIndicator(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            pagerState = pagerState
        )

        FinishButton(
            modifier = modifier.weight(1f),
            pagerState = pagerState,
            onClick = {
                mWViewModel.saveOnBoardingState(completed = true)
                navHostController.popBackStack()
                navHostController.navigate("Main")
            }
        )
    }
}

@Composable
private fun Pager(onBoardingPage: OnBoardingPage, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.8f),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Pager Image"
        )

        Text(
            modifier = modifier
                .fillMaxWidth(),
            text = onBoardingPage.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = onBoardingPage.description,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FinishButton(pagerState: PagerState, onClick: () -> Unit, modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = pagerState.currentPage == 2
        ) {
            Button(onClick = { onClick() }) {
                Text(text = "Finish")
            }
        }
    }
}