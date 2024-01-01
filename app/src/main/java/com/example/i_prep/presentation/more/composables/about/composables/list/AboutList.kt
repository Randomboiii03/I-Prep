package com.example.i_prep.presentation.more.composables.about.composables.list

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.i_prep.R
import com.example.i_prep.common.githubRepo
import com.example.i_prep.common.latestVersion
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.more.composables.about.composables.list.components.BItem
import com.example.i_prep.presentation.more.composables.about.model.AboutNav
import com.example.i_prep.presentation.more.composables.about.model.aboutNav
import com.example.i_prep.presentation.more.composables.components.MTopBar
import com.randomboiii.i_prep.presentation.use_case.ConnectionState
import com.randomboiii.i_prep.presentation.use_case.connectivityState

@Composable
fun AboutList(
    onBack: () -> Unit,
    globalEvent: (GlobalEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available

    val context = LocalContext.current

    Scaffold(
        topBar = { MTopBar(onBack = { onBack() }, title = "About") }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier.padding(vertical = 50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = modifier.size(125.dp)
                )
            }

            Divider()

            LazyColumn {
                item {
                    BItem(
                        title = AboutNav.Version.title,
                        onClickItem = {
                            Toast.makeText(
                                context,
                                "I-Prep v$latestVersion",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                }

                item {
                    BItem(
                        title = AboutNav.CheckUpdate.title,
                        onClickItem = {
                            when (isConnected) {
                                true -> globalEvent(GlobalEvent.CheckUpdate(context))
                                false -> Toast.makeText(
                                    context,
                                    "Check internet connection",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }

                item {
                    BItem(
                        title = AboutNav.PrivacyPolicy.title,
                        onClickItem = { navHostController.navigate(AboutNav.PrivacyPolicy.title) })
                }
            }

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = "Back",
                    modifier = modifier
                        .padding(vertical = 16.dp)
                        .padding(end = 8.dp)
                        .size(30.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(githubRepo)))
                        }
                )

                Text(text = "Github", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}