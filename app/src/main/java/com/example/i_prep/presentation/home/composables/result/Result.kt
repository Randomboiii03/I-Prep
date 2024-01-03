package com.example.i_prep.presentation.home.composables.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.i_prep.R
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.GlobalState
import com.example.i_prep.presentation.home.composables.result.components.RTopBar
import com.example.i_prep.presentation.home.composables.test.TEvent
import com.example.i_prep.presentation.home.composables.test.TViewModel
import com.example.i_prep.presentation.home.model.HomeNav

@Composable
fun Result(
    score: Int,
    itemSet: Int,
    onBack: () -> Unit,
    onResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    val percentage = ((score.toDouble() / itemSet.toDouble()) * 100).toInt()

    Scaffold(
        topBar = { RTopBar { onBack() } }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .wrapContentSize()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(16.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.trophy),
                    contentDescription = "trophy",
                    modifier = modifier.size(150.dp)
                )

                Text(
                    text = if (percentage >= 75) "Congrats!" else "Study Hard!",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "$percentage% score",
                    color = if (percentage >= 75) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 36.sp
                )

                Text(
                    text = "You completed it successfully.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )

                Text(
                    buildAnnotatedString {
                        append("You attempt ")

                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("${itemSet} questions")
                        }

                        append(" and")
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                )

                Text(
                    buildAnnotatedString {
                        append("from that ")

                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("${score} answer")
                        }

                        append(" is correct.")
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    modifier = modifier.padding(bottom = 12.dp)
                )

                Button(
                    onClick = { onResult() },
                    shape = RoundedCornerShape(16.dp),
                    modifier = modifier.padding(bottom = 12.dp)
                ) {
                    Text(text = "View Result")
                }
            }
        }
    }
}