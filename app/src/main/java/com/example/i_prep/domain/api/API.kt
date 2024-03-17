package com.example.i_prep.domain.api


import android.util.Log
import com.example.i_prep.common.gson
import com.example.i_prep.domain.api.model.dto.Details
import com.example.i_prep.domain.api.model.dto.Question
import com.example.i_prep.domain.api.model.dto.TestInfo
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.reflect.TypeToken

class IPrepAPI {
    
    fun fixFormat(response: String): String {
        val startIndex = response.indexOf('{')
        val endIndex = response.lastIndexOf('}') + 1

        return response.substring(startIndex, endIndex)
    }

    suspend fun generate(
        question_type: String,
        difficulty: String,
        language: String,
        topic: String
    ): TestInfo {
        val model = GenerativeModel(
            "gemini-1.0-pro",
            "AIzaSyCyXbK3_7YcA2oX_VBcRWU9kyGSzHvahY8AIzaSyCyXbK3_7YcA2oX_VBcRWU9kyGSzHvahY8",
            generationConfig = generationConfig {
                temperature = 0.9f
                topK = 1
                topP = 1f
                maxOutputTokens = 2048
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
            ),
        )

        val prompt =
            """Your task is to create a ${difficulty} ${language} $question_type} practice test based on the provided study material. 
            The study material will contain the all the information that will show on the practice test, including the topic and subject. 
            Implement the proper creation of stem questions, ensuring that there is no repetitive questions or answer, perform necessary validations, and follow best practices for creating a proper practice test. 
            Please make sure to follow the question type, difficulty, language, and subject matter for the creation of test. 
            Please output only in this JSON format: 
            {
                "questions": [
                {
                    "question": "",
                    "choices": [],
                    "answer": ""
                }
                ]
            }
            Another task, if the user send "DETAILS" you will create a test title, description and tags (purpose, subject, level, format) in this JSON format:
            {
                "title": "",
                "description": "",
                "tags": ["",""]
            } 
            Another task, if the user send \"MORE\" you will create more unique test questions with the same topic. If you understand, respond "OK".
            
        """.trimIndent()

        val respond =
            """"OK, I understand. Please provide the study material so I can generate practice test questions in the requested JSON format. 
            Remember to specify the desired question types, difficulty, language, and subject matter questions. 
            If you want more unique questions on the same topic, simply send "MORE" and I will generate additional questions based on the provided material.
            If you want the details, simply send "DETAILS" and I will generate title, description and tags based on the generated test.
        """.trimIndent()

        val chatHistory = listOf(
            content("user") {
                text(prompt)
            },
            content("model") {
                text(respond)
            },
        )

        val chat = model.startChat(chatHistory)
        var response = chat.sendMessage(topic)

        val questionType = object : TypeToken<List<Question>>() {}.type
        val existingQuestions: MutableList<Question> = gson.fromJson(fixFormat(response.text.toString()), questionType)

        Log.v("TAG", existingQuestions.toString())

        val history = listOf(prompt, respond)

        var tokenCount = 0

        history.forEach { messsage ->
            tokenCount += model.countTokens(messsage).totalTokens
        }

        while (tokenCount <= 10000) {
            response = chat.sendMessage("MORE")
            val questions: List<Question> = gson.fromJson(fixFormat(response.text.toString()), questionType)
            existingQuestions.addAll(questions)
            Log.v("TAG", response.text.toString())
            tokenCount += model.countTokens(response.text.toString()).totalTokens
        }

        response = chat.sendMessage("DETAILS")
        val details: Details = gson.fromJson(fixFormat(response.text.toString()), Details::class.java)

        val testInfo = TestInfo(
            title = details.title,
            description = details.description,
            tags = details.tags,
            questions = existingQuestions
        )

        Log.v("TAG", testInfo.toString())

        return testInfo
    }
}