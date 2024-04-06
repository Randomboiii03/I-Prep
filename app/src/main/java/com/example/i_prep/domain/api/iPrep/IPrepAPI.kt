package com.example.i_prep.domain.api.iPrep


import com.example.i_prep.BuildConfig
import com.example.i_prep.common.displayLog
import com.example.i_prep.common.gson
import com.example.i_prep.domain.api.iPrep.model.Details
import com.example.i_prep.domain.api.iPrep.model.Question
import com.example.i_prep.domain.api.iPrep.model.QuestionList
import com.example.i_prep.domain.api.iPrep.model.TestInfo
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random

class IPrepAPI {

    private val randomNumber = Random.nextLong(3000L, 5001L)

    private fun fixFormat(response: String): String {
        val startIndex = response.indexOf('{')
        val endIndex = response.lastIndexOf('}') + 1

        return response.substring(startIndex, endIndex)
    }

    private fun getImage(): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://random.imagecdn.app/v1/image?width=400&height=600&category=nature")
            .build()
        val response = client.newCall(request).execute()

        return response.body.string().split('?')[0]
    }

    suspend fun generate(
        questionType: String,
        difficulty: String,
        language: String,
        topic: String
    ): TestInfo {
        val model = GenerativeModel(
            modelName = "gemini-1.0-pro",
            apiKey = BuildConfig.geminiAIKey,
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

        val jsonFormat = when(questionType) {
            "True or False" -> {"""
                {
                    "questions": [
                        {
                            "question": "",
                            "choices": ["True", "False"], // True or False only
                            "answer": ""
                        }
                    ]
                }
            """.trimIndent()}

            else -> {"""
                {
                    "questions": [
                        {
                            "question": "",
                            "choices": ["", "", "", ""],
                            "answer": ""
                        }
                    ]
                }
            """.trimIndent()}
        }

        val prompt =
            """This guide equips you to transform limited study materials into a springboard for active learning. Move beyond rote memorization by crafting questions that challenge you to:

            - Apply concepts: How can this be used in the real world?
            - Analyze strengths and weaknesses: What are the pros and cons of this idea?
            - Evaluate persuasiveness: Is the argument convincing? Why or why not?
            - Predict or Infer from the material: What might happen next? Can you draw conclusions about something not mentioned?
            - Make Connections to prior knowledge: How does this compare to what you already know?
            - Identify Gaps in Knowledge for further exploration: What caused this event? What are the unaddressed implications? Are there counterarguments?
            - Explore Different Question Formats (open ended, true/false, matching) to test your understanding in diverse ways.
                
            Imagine you're the teacher, and your limited study materials are the only resource your students have.
            Your task is to create a $difficulty $language $questionType practice test based on the provided study material.
            Craft questions that would push them beyond memorization and into active engagement with the material. 
            The study material will contain the all the information that will show on the practice test, including the topic and subject. 
            Implement the proper creation of stem questions, ensure that there is no repetitive questions or answer and perform necessary validations.
            Please follow best practices for creating a proper practice test use Fink's Taxonomy of Significant Learning as a framework and SOLO Taxonomy.
            
            Please make sure to follow the question type, difficulty, language, and subject matter for the creation of test. 
            Please output only in this JSON format: $jsonFormat
            Another task, if the user send "DETAILS" you will create a test title, short description, and tags (purpose, subject, level, format, etc.) in this JSON format:
            {
                "title": "",
                "description": "",
                "tags": ["",""]
            } 
            Another task, if the user send \"MORE\" you will create more unique test questions with the same topic. If you understand all of this, respond "OK".
            Utilize the strategies above to create a stimulating learning experience! Thank you, you're the best!
        """.trimIndent()

        val respond =
            """"OK, I understand. Please provide the study material so I can generate $difficulty $language $questionType practice test questions in the requested JSON format. 
            If you want more unique and non-repetitive questions or answer on the same topic, simply send "MORE" and I will generate additional questions based on the provided material.
            If you want the details, simply send "DETAILS" and I will generate title, short description, and tags based on the generated test.
        """.trimIndent()

        val chatHistory = listOf(
            content("user") {
                text(prompt)
            },
            content("model") {
                text(respond)
            },
        )

        delay(randomNumber)

        val chat = model.startChat(chatHistory)
        var response = chat.sendMessage(topic)

        val combinedQuestions = mutableListOf<Question>()
        combinedQuestions.addAll(gson.fromJson(fixFormat(response.text.toString()), QuestionList::class.java).questions)

        val (tokens) = model.countTokens(*chat.history.toTypedArray())
        var tokenCount = tokens

        while (tokenCount <= 25000) {
            try {
                delay(randomNumber)

                response = chat.sendMessage("MORE")
                combinedQuestions.addAll(gson.fromJson(fixFormat(response.text.toString()), QuestionList::class.java).questions)

                val (token) = model.countTokens(*chat.history.toTypedArray())
                tokenCount = token

                displayLog("runAPI", "Size: ${combinedQuestions.size} Token Count: $tokenCount")

            } catch (e: Exception) {
                displayLog("runAPI", "Failed, Retrying again")
            }
        }

        delay(randomNumber)

        response = chat.sendMessage("DETAILS")
        val details = gson.fromJson(fixFormat(response.text.toString()), Details::class.java)

        displayLog("runAPI", details.toString())

        delay(randomNumber)

        val image = getImage()

        displayLog("runAPI", "image")

        val testInfo = TestInfo(
            title = details.title,
            description = details.description,
            tags = details.tags,
            questions = combinedQuestions.toSet().toList(),
            image = image
        )

        displayLog("runAPI", testInfo.toString())

        return testInfo
    }
}