package com.example.i_prep.presentation.create.composables.form.components

import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.example.i_prep.presentation.create.CEvent
import java.io.File
import java.io.FileOutputStream

@Composable
fun FUploadFile(fileName: String, onEvent: (CEvent) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            var cacheDir = context.cacheDir

            // Need storage checker if there is space for the file

            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                    val fileName = it.getString(columnIndex)
                    val fileExtension = File(fileName).extension

                    if (fileExtension in arrayListOf("pdf", "docx", "txt", "xlsx", "csv")) {

                        try {
                            val inputStream = contentResolver.openInputStream(uri)
                            val fileSize = inputStream?.available() ?: 0
                            inputStream?.close()

                            if (fileSize <= 10000000) {
                                val folder = File(cacheDir, "uploaded_files")

                                if (!folder.exists()) {
                                    folder.mkdir()
                                }

                                val file = File(folder, fileName)

                                try {
                                    val inputStream = contentResolver.openInputStream(uri)
                                    val outputStream = FileOutputStream(file)
                                    onEvent(CEvent.UploadFile(fileName, file.absolutePath))

                                    inputStream?.use { input ->
                                        outputStream.use { output ->
                                            input.copyTo(output)
                                        }
                                    }

                                    Toast.makeText(context, fileName, Toast.LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error: $e", Toast.LENGTH_LONG).show()
                                }

                            } else {
                                Toast.makeText(
                                    context,
                                    "File must be 10MB or less only",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Log.v("TAG - CTUploadFile", "$e")
                        }

                    } else {
                        Toast.makeText(context, "Can't select this type of file", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    OutlinedTextField(
        value = fileName,
        onValueChange = {},
        readOnly = true,
        maxLines = 1,
        label = { Text(text = "Reference File") },
        trailingIcon = {
            Icon(imageVector = Icons.Default.UploadFile, contentDescription = "Upload File")
        },
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { launcher?.launch("*/*") }
    )
}