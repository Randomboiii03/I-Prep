package com.example.i_prep.presentation.create.components

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.i_prep.common.displayLog
import com.example.i_prep.common.displayToast
import com.example.i_prep.common.extractPDF
import com.example.i_prep.common.extractTXT
import com.example.i_prep.presentation.create.CEvent
import com.example.i_prep.presentation.create.CState
import java.io.File
import java.io.FileOutputStream

@Composable
fun CUploadFile(state: CState, onEvent: (CEvent) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)

            // Need storage checker if there is space for the file

            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                    val fileName = it.getString(columnIndex)
                    val fileExtension = File(fileName).extension

                    if (fileExtension in arrayListOf("pdf", "docx", "txt")) {
                        try {
                            val inputStream = contentResolver.openInputStream(uri)
                            val fileSize = inputStream?.available() ?: 0

                            if (fileSize <= 10000000) {
                                val folder = File(context.cacheDir, "uploaded_files").apply {
                                    if (!exists()) mkdir()
                                }

                                val file = File(folder, fileName)

                                inputStream?.use { input ->
                                    FileOutputStream(file).use { output ->
                                        input.copyTo(output)
                                    }
                                }

                                val reference = when (fileExtension) {
                                    "pdf" -> extractPDF(file.absolutePath)
                                    "txt" -> extractTXT(file.absolutePath)
                                    else -> ""
                                }

                                displayLog("CTUploadFile", reference.toString())

                                if (!reference.isNullOrEmpty() && reference.isNotBlank()) {
                                    onEvent(CEvent.SetForm(state.copy(fileName = fileName, reference = reference.toString())))
                                    displayToast(fileName, context)

                                } else displayToast("No text extracted", context)

                                file.delete()

                            } else displayToast("File must be 10MB or less only", context)

                        } catch (e: Exception) {
                            displayLog("CTUploadFile", "Error: $e")
                        }

                    } else displayToast("Can't select this type of file", context)
                }
            }
        }
    }

    OutlinedTextField(
        value = state.fileName,
        onValueChange = {},
        readOnly = true,
        maxLines = 1,
        label = { Text(text = "Reference File") },
        trailingIcon = {
            Icon(imageVector = Icons.Default.UploadFile, contentDescription = "Upload File")
        },
        supportingText = {
            Text(text = "Upload document file (e.g. PDF or TXT)")
        },
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .width(280.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { launcher.launch("application/pdf") }
    )
}