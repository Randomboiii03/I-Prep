package com.example.i_prep.domain.api.iPrep


import com.example.i_prep.BuildConfig
import com.example.i_prep.common.displayLog
import com.example.i_prep.common.gson
import com.example.i_prep.domain.api.iPrep.model.Details
import com.example.i_prep.domain.api.iPrep.model.Question
import com.example.i_prep.domain.api.iPrep.model.QuestionList
import com.example.i_prep.domain.api.iPrep.model.TestInfo
import com.google.ai.client.generativeai.Chat
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

    private val timeDelay = Random.nextLong(5000L, 10001L)
//    private val timeDelay = 10000L

    var tokenCount = 0

    private var model: GenerativeModel? = null

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

    suspend fun setupChat(
        questionType: String,
        difficulty: String,
        language: String,
        topic: String
    ): Chat {
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

        val guide = when(questionType) {
            "True or False" -> {"""
                - Identify a Key Concept: Pick a core idea from your study material.
                - Twist it Up: Craft a statement that is either completely true or completely false based on the concept.
                - True: This could be a direct restatement of a fact from the material.
                - False: Here's where it gets interesting! You can slightly twist a fact, introduce a logical fallacy, or contradict an underlying assumption.
                - Plant the Seed of Doubt: Make the statement believable enough to create a healthy debate among your classmates.
                - Justification is Key: Provide a brief explanation for why the statement is true or false. This will guide your classmates towards the key concept and encourage them to revisit the material for deeper understanding.
            """.trimIndent()}

            "Multiple Choice" -> {"""
                - Apply concepts: How can this be used in the real world?
                - Analyze strengths and weaknesses: What are the pros and cons of this idea?
                - Evaluate persuasiveness: Is the argument convincing? Why or why not?
                - Predict or Infer from the material: What might happen next? Can you draw conclusions about something not mentioned?
                - Make Connections to prior knowledge: How does this compare to what you already know?
                - Identify Gaps in Knowledge for further exploration: What caused this event? What are the unaddressed implications? Are there counterarguments?
            """.trimIndent()}

            else -> {"""
                - Identify a Core Process or Formula:  Find a central process or formula  presented in your study material. This will be the framework for your fill-in-the-blank.
                - Leave a Strategic Gap:  Choose a crucial element within the process or formula and replace it with a blank. This blank should be significant enough to require understanding,  but not so obscure that it becomes impossible to answer.
                - Maintain Clarity:  Don't  overload the question with too many blanks. Aim for one or two blanks at most to ensure the question remains clear and focused.
                - Offer Multiple Answer Choices (Optional):  While fill-in-the-blank questions traditionally involve open-ended answers, you can consider including a limited set of answer choices  to guide your classmates. This can be particularly helpful with limited material  as it narrows down the possibilities and reinforces key terms.
                - Think Beyond Straight Recall:  While some blanks might  require recalling specific terms, aim to also  include blanks that encourage applying or analyzing the information.
            """.trimIndent()}
        }

        val systemPrompt =
            """This guide equips you to transform limited study materials into a springboard for active learning. 
            Move beyond rote memorization by crafting $questionType questions that challenge you to:
            $guide
                
            Imagine you're the teacher, and your limited study materials are the only resource your students have.
            Your task is to create a $difficulty $language $questionType practice test based on the provided study material.
            Craft questions that would push them beyond memorization and into active engagement with the material. 
            The study material will contain the all the information that will show on the practice test, including the topic and subject. 
            Implement the proper creation of stem questions, ensure that there is no repetitive questions or answer and perform necessary validations.
            Please follow best practices for creating a proper practice test use Fink's Taxonomy of Significant Learning as a framework and SOLO Taxonomy.
            
            Please make sure to follow the question type, difficulty, language, and subject matter for the creation of test. 
            Please output only in this JSON format in the language of $language: $jsonFormat
            Another task, if the user send "DETAILS" you will create a test short title, short description, and tags (purpose, subject, level, format, etc.) in this JSON format:
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
                text("""Below is the topic in a language "$language":
                    $topic
                    Please analyze and understand the subject matter that the topic have.
                """.trimMargin())
            },
            content("model") {
                text("""Nice! This study material is very informative. If you want to start creating $difficulty $language $questionType practice test questions, simply send "START" and I will start.""")
            }
        )

        model = GenerativeModel(
            modelName = "gemini-1.5-pro-latest",
            apiKey = BuildConfig.geminiAIKey,
            generationConfig = generationConfig {
                temperature = 1f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "text/plain"
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.LOW_AND_ABOVE),
            ),
            systemInstruction = content { text(systemPrompt) }
        )

        val chat = model!!.startChat(chatHistory)

        val (tokens) = model!!.countTokens(*chat.history.toTypedArray())
        tokenCount = tokens

        delay(timeDelay)

        return chat
    }

    suspend fun generate(chat: Chat): TestInfo? {
        var response = chat.sendMessage("START")
        displayLog("RESPONSE", response.text.toString())
        val combinedQuestions = mutableListOf<Question>()
        combinedQuestions.addAll(gson.fromJson(fixFormat(response.text.toString()), QuestionList::class.java).questions)

        if (model == null) {
            return null
        }

        val (tokens) = model!!.countTokens(*chat.history.toTypedArray())
        tokenCount = tokens
        val tokenLimit = tokens + 8500

        var attempt = 0

        while (tokenCount <= tokenLimit) {
            try {
                delay(timeDelay)

                response = chat.sendMessage("MORE")
                combinedQuestions.addAll(gson.fromJson(fixFormat(response.text.toString()), QuestionList::class.java).questions)

                val (token) = model!!.countTokens(*chat.history.toTypedArray())
                tokenCount = token
                displayLog("RESPONSE", response.text.toString())
                displayLog("runAPI", "Size: ${combinedQuestions.count()} Token Count: $tokenCount")

            } catch (e: Exception) {
                displayLog("runAPI", "Error-generate: $e")
                displayLog("runAPI", "Failed, Retrying again")
                attempt++

                if (attempt >= 10) break
            }
        }

        delay(timeDelay)

        attempt = 0

        var details: Details? = null

        while (attempt <= 5) {
            try {
                response = chat.sendMessage("DETAILS")
                details = gson.fromJson(fixFormat(response.text.toString()), Details::class.java)

                displayLog("runAPI", details.toString())
                break

            } catch (e: Exception) {
                displayLog("runAPI", "Error-generate: $e")
                displayLog("runAPI", "Failed, Retrying again")
                attempt++
            }
        }

        delay(timeDelay)

        val image = getImage()

        displayLog("runAPI", "image")

        if (details != null) {
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

        return null
    }
}