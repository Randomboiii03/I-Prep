package com.example.i_prep.presentation.more.composables.options

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.i_prep.R
import com.example.i_prep.presentation.more.composables.options.components.OItem
import com.example.i_prep.presentation.more.model.moreNav

@Composable
fun Options(modifier: Modifier = Modifier) {
    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier.padding(vertical = 75.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", modifier = modifier.size(125.dp))
            }

            Divider()

            LazyColumn {
                items(moreNav) { item ->
                    OItem(title = item.title, icon = item.icon, onClickItem = { /*TODO*/ })
                }
            }
        }
    }
}