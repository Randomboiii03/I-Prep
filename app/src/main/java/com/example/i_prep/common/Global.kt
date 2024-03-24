package com.example.i_prep.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream


val gsonBuilder: GsonBuilder = GsonBuilder().apply {
    setPrettyPrinting()
    setLenient()
    setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
}

val gson: Gson = gsonBuilder.create()

fun String.compareToVersion(version: String): Boolean {
    val latestVersion = this.split(".").mapNotNull { it.toIntOrNull() }
    val updateVersion = version.split(".").mapNotNull { it.toIntOrNull() }

    val minLength = minOf(latestVersion.size, updateVersion.size)

    for (i in 0 until minLength) {
        val latestComponent = latestVersion[i]
        val updateComponent = updateVersion[i]

        if (latestComponent < updateComponent) {
            return true
        }
    }

    return false
}

fun compressAndEncode(input: String): String {
    val outputStream = ByteArrayOutputStream()
    GZIPOutputStream(outputStream).bufferedWriter(StandardCharsets.UTF_8).use {
        it.write(input)
    }
    val compressedBytes = outputStream.toByteArray()

    val base64Encoded = Base64.getEncoder().encodeToString(compressedBytes)

    return base64Encoded
}

fun decodeAndDecompress(encodedInput: String): String {
    val base64Decoded = Base64.getDecoder().decode(encodedInput)

    val inputStream = ByteArrayInputStream(base64Decoded)
    return GZIPInputStream(inputStream).bufferedReader(StandardCharsets.UTF_8).use {
        it.readText()
    }
}

//@SuppressLint("QueryPermissionsNeeded")
//fun shareFile(context: Context, fileName: String, content: String) {
//
//    val folder = File(context.cacheDir, "shared_files")
//
//    if (!folder.exists()) folder.mkdirs()
//
//    val file = File(folder, "$fileName.txt")
//
//    try {
//        FileOutputStream(file).use { stream ->
//            stream.write(content.toByteArray())  // Write string to file
//        }
//    } catch (e: IOException) {
//        // Handle exception if file creation or writing fails
//        Log.e("ShareText", "Error creating or writing to file", e)
//    }
//
//    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
//
//    val intent = Intent(Intent.ACTION_SEND).apply {
//        type = "text/plain"  // Set MIME type for text file
//        putExtra(Intent.EXTRA_STREAM, uri)
//        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//    }
//
//    val chooser = Intent.createChooser(intent, "Share File")
//
//    val resInfoList: List<ResolveInfo> =
//        context.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
//
//    for (resolveInfo in resInfoList) {
//        val packageName = resolveInfo.activityInfo.packageName
//        context.grantUriPermission(
//            packageName,
//            uri,
//            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
//        )
//    }
//
//    context.startActivity(chooser)
//}

fun extractPDF(filePath: String): String? {
    try {
        var extractedText = ""

        val pdfReader = PdfReader(filePath)
        val numPages = pdfReader.numberOfPages

        for (i in 0 until numPages) {
            extractedText = """
                    $extractedText${
                PdfTextExtractor.getTextFromPage(pdfReader, i + 1).trim { it <= ' ' }
            }
                """.trimIndent()
        }

        pdfReader.close()

        return extractedText

    } catch (e: Exception) {
        Log.v("TAG", "Error: $e")
        return null
    }
}

fun extractTXT(filePath: String): String? {
    try {
        val file = File(filePath)
        return file.readText()

    } catch (e: Exception) {
        Log.v("TAG", "Error: $e")
        return null
    }
}

fun displayToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun displayLog(from: String, message: String) {
    Log.v("TAG - $from", message)
}