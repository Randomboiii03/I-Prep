package com.example.i_prep.presentation.create.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.common.components.ItemDivider
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.create.form.components.FDropdown
import com.example.i_prep.presentation.create.form.components.FTopBar
import com.example.i_prep.presentation.create.form.components.FUploadFile
import com.example.i_prep.presentation.create.form.model.difficulties
import com.example.i_prep.presentation.create.form.model.languages
import com.example.i_prep.presentation.create.form.model.questionTypes

@Composable
fun Form(globalEvent: (GlobalEvent) -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { FTopBar(onReset = {}) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "Create Test", style = MaterialTheme.typography.titleLarge)

            Divider(modifier = modifier.padding(horizontal = 16.dp))

            FDropdown(
                value = "",
                onValueChange = {},
                list = questionTypes,
                label = "Question Types"
            )

            FUploadFile(fileName = "")

            FDropdown(
                value = "",
                onValueChange = {},
                list = languages,
                label = "Language"
            )

            FDropdown(
                value = "Easy",
                onValueChange = {},
                list = difficulties,
                label = "Difficulty"
            )

            Divider(modifier = modifier.padding(horizontal = 16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(onClick = { /*TODO*/ }, modifier = modifier.width(107.dp)) {
                    Text(text = "Reset")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Generate")
                }
            }
        }
    }
}