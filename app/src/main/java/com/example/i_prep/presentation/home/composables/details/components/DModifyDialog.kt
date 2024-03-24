package com.example.i_prep.presentation.home.composables.details.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.i_prep.data.local.model.PTest

@SuppressLint("UnrememberedMutableState")
@Composable
fun DModifyDialog(
    pTest: PTest,
    onDismiss: (Boolean) -> Unit,
    onModify: (PTest) -> Unit,
    modifier: Modifier = Modifier
) {
    val title = mutableStateOf(pTest.title)
    val itemSet = mutableIntStateOf(pTest.itemSet)
    val isTimed = mutableStateOf(pTest.isTimed)

    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss(false)
                    onModify(pTest.copy(title = title.value, itemSet = itemSet.intValue, isTimed = isTimed.value))
                },
                enabled = title.value.isNotEmpty() && itemSet.intValue > 0
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onDismiss(false) }) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(
                text = "Modify Test",
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text(text = "Title") },
                    isError = title.value.isEmpty())

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = "${itemSet.intValue}",
                        suffix = { Text(text = "/ ${pTest.totalItems}") },
                        onValueChange = { it ->
                            Log.v("TAG", it)
                            val value = it.filter { it.isDigit() }
                            if (value.isNotEmpty()) {
                                if (value.toInt() <= pTest.totalItems) {
                                    itemSet.intValue = value.toInt()
                                }
                            } else itemSet.intValue = 0
                        },
                        label = { Text(text = "Item Set") },
                        isError = itemSet.intValue <= 0,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = modifier.weight(1f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isTimed.value,
                            onCheckedChange = { isTimed.value = !isTimed.value })

                        Text(text = "Timed Test")
                    }
                }
            }
        })
}