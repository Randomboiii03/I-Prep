package com.example.i_prep.presentation.create

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.i_prep.domain.api.IPrepAPI
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class CViewModel : ViewModel() {

    private val _state: MutableStateFlow<CState> = MutableStateFlow(CState())
    val state = _state

    fun onReset(): Boolean {
        return state.value.questionType.isNotEmpty() || (state.value.fileName.isNotEmpty() && state.value.filePath.isNotEmpty()) || state.value.language.isNotEmpty() || state.value.difficulty != "Easy"
    }

    fun onGenerate(): Boolean {
        return state.value.questionType.isNotEmpty() && (state.value.fileName.isNotEmpty() && state.value.filePath.isNotEmpty()) && state.value.language.isNotEmpty()
    }

    private fun extractPDF(filePath: String): String? {
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

    private fun extractTXT(filePath: String): String? {
        try {
            val file = File(filePath)
            return file.readText()

        } catch (e: Exception) {
            Log.v("TAG", "Error: $e")
            return null
        }
    }

    fun onEvent(event: CEvent) {
        when (event) {
            CEvent.Reset -> _state.update {
                it.copy(
                    questionType = "",
                    fileName = "",
                    filePath = "",
                    language = "",
                    difficulty = "Easy",
                    cookie = ""
                )
            }

            is CEvent.SetDifficulty -> _state.update { it.copy(difficulty = event.difficulty) }
            is CEvent.SetLanguage -> _state.update { it.copy(language = event.language) }
            is CEvent.SetQuestionType -> _state.update { it.copy(questionType = event.questionType) }
            is CEvent.UploadFile -> _state.update {
                it.copy(fileName = event.fileName, filePath = event.filePath)
            }

            is CEvent.SetCookie -> state.update { it.copy(cookie = event.cookie) }
            is CEvent.Generate -> _state.update { it.copy(isGenerate = event.isGenerate) }
        }
    }

    suspend fun runAPI() {
        val fileExtension = File(state.value.fileName).extension
        Log.v("TAG", "Extension: $fileExtension")
        val topic = when (fileExtension) {
            "pdf" -> extractPDF(state.value.filePath)
            "txt" -> extractTXT(state.value.filePath)
            else -> ""
        }

        Log.v("TAG", topic.toString())

        if (!topic.isNullOrEmpty()) {
            val testInfo = IPrepAPI().generate(
                question_type = state.value.questionType,
                difficulty = state.value.difficulty,
                language = state.value.language,
                topic = topic
            )
        } else {
            Log.v("TAG", "NO TEXT EXTRACTED")
        }

        _state.update { it.copy(isGenerate = false) }
    }
}

data class CState(
    val questionType: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val language: String = "",
    val difficulty: String = "Easy",
    val cookie: String = "",
    val isGenerate: Boolean = false
)

sealed interface CEvent {
    object Reset : CEvent
    data class SetQuestionType(val questionType: String) : CEvent
    data class UploadFile(val fileName: String, val filePath: String) : CEvent
    data class SetLanguage(val language: String) : CEvent
    data class SetDifficulty(val difficulty: String) : CEvent
    data class SetCookie(val cookie: String) : CEvent
    data class Generate(val isGenerate: Boolean) : CEvent
}