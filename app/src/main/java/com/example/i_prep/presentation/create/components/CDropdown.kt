package com.example.i_prep.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    list: List<String>,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                label = { Text(text = label) },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = modifier.menuAnchor()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                list.forEachIndexed { index, dropdownItem ->
                    DropdownMenuItem(
                        text = { Text(text = dropdownItem, textAlign = TextAlign.Center) },
                        colors = MenuDefaults.itemColors(textColor = if (dropdownItem == value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface),
                        onClick = {
                            onValueChange(dropdownItem)
                            expanded = false
                        },
                        modifier = modifier.background(if (dropdownItem == value) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                    )
                }
            }
        }
    }
}