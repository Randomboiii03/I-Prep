package com.example.i_prep.common

import androidx.compose.runtime.mutableStateOf
import com.example.i_prep.presentation.create.composables.form.model.questionTypes

fun getPrompt(version: Int, questionType: String, difficulty: String, language: String): String {
    val prompt = mutableStateOf("")

    when (version) {
        1 -> { // Source: http://uhhcopfacultyresource.weebly.com/uploads/2/1/9/8/2198211/multiple_choice_and_true_false_exam_question_design_booklet.pdf
            val rules = mutableStateOf("")

            when (questionType) {
                "mcq" -> {
                    rules.value = """
                        <rules>
                        - Ensure question variety by limiting "W" questions (Who, What, When, Where, Why) to 30% or less of total
                        - For documents lacking real-world context or documents containing many abstract examples (math, physics, code), create applied scenario-based question stems
                        - Produce questions that cover all key aspects of the uploaded document
                        - Write stems clearly and concisely without irrelevant information 
                        - Avoid cultural/language biases and complex vocabulary 
                        - Highlight or avoid negatives in stems 
                        - Use consistent grammar between stem and all options 
                        - Keep response options similar in length 
                        - Ensure distractors are plausible 
                        - Make options mutually exclusive 
                        - Have only one clearly best answer 
                        - Use common mistakes as distractors 
                        - Avoid lifting options directly from textbooks 
                        - Use "all of the above" and "none of the above" carefully 
                        - Avoid "I don't know" as an option
                        </rules>
                    
                        <json>
                        ```
                        {
                            "title": "unique test title",
                            "tags": ["subject", "topic", "genre", "difficulty", "question-type", "add more"],
                            "language": "$language",
                            "description": "short yet detailed information that describe the test",
                            "questions":
                            [
                                {
                                    "question": "question stem1",
                                    "choice": ["choice1", "choice2", "choice3", "choice4"],
                                    "answer": "index number of the correct answer from choices"
                                }
                            ]
                        }
                        ```
                        </json>
                    """.trimIndent()
                }

                "tof" -> {
                    rules.value = """
                        <rules>
                        - Ensure question variety by limiting "W" questions (Who, What, When, Where, Why) to 30% or less of total
                        - For documents lacking real-world context or documents containing many abstract examples (math, physics, code), create applied scenario-based question stems
                        - Produce questions that cover all key aspects of the uploaded document
                        - Keep stems relatively short and simple 
                        - Avoid sweeping broad statements or absolutes which are often false 
                        - Avoid ambiguous or vague statements 
                        - Do not arrange questions in a predictable pattern of answers 
                        - Use True/False or Yes/No response options consistently 
                        - Avoid negatively worded stems or highlight negative words 
                        - Test lower level cognitive skills like knowledge, comprehension and application 
                        - Apply principles for writing good MCQs like avoiding irrelevant information 
                        - Use cause/effect format to test understanding of relationships 
                        </rules>
                    
                        <json>
                        ```
                        {
                            "title": "unique test title",
                            "tags": ["subject", "topic", "genre", "difficulty", "question-type", "add more"],
                            "language": "$language",
                            "description": "short yet detailed information that describe the test",
                            "questions":
                            [
                                {
                                    "question": "question stem1",
                                    "answer": "true/false"
                                }
                            ]
                        }
                        ```
                        </json>
                    """.trimIndent()
                }

                "sa" -> {
                    rules.value = """
                        <rules>
                        - Ensure question variety by limiting "W" questions (Who, What, When, Where, Why) to 30% or less of total
                        - For documents lacking real-world context or documents containing many abstract examples (math, physics, code), create applied scenario-based question stems
                        - Produce questions that cover all key aspects of the uploaded document
                        - Ensure the stem clearly indicates where and what students need to fill in 
                        - Do not make blanks overly long or short 
                        - Position blanks logically at the end of phrases or clauses 
                        - Use blanks to test key terms, facts, concepts 
                        - Avoid using blanks for opinion-based or interpretative responses 
                        - Craft questions so there is only one appropriate response for each blank 
                        - Avoid negatively phrased stems with blanks 
                        - Provide clear instructions if capitalization or spelling precision are required
                        </rules>
                    
                        <json>
                        ```
                        {
                            "title": "unique test title",
                            "tags": ["subject", "topic", "genre", "difficulty", "question-type", "add more"],
                            "language": "$language",
                            "description": "short yet detailed information that describe the test",
                            "questions":
                            [
                                {
                                    "question": "question stem1",
                                    "answer": "correct answer"
                                }
                            ]
                        }
                        ```
                        </json>
                    """.trimIndent()
                }

                "fitb" -> {
                    rules.value = """
                        <rules>
                        - Ensure question variety by limiting "W" questions (Who, What, When, Where, Why) to 30% or less of total
                        - For documents lacking real-world context or documents containing many abstract examples (math, physics, code), create applied scenario-based question stems
                        - Produce questions that cover all key aspects of the uploaded document
                        - Craft questions that assess higher-order skills like application, analysis, synthesis 
                        - Make questions as specific and narrowly focused as possible 
                        - Use the questions to target key concepts or examples from content 
                        - Pose questions that require students to explain, compare, demonstrate, or interpret 
                        - Ask questions that stimulate new thinking beyond just facts 
                        - Design questions with scope for varied approaches in responses 
                        - Construct questions with clear wording and terminology 
                        - Allocate sufficient space for desired length of responses 
                        - Indicate how much detail, supporting evidence, explanations are required 
                        - Develop a scoring rubric aligned to questions beforehand 
                        - Set expectations for required components of acceptable responses 
                        - Allow flexibility for well- reasoned alternative responses
                        </rules>
                    
                        <json>
                        ```
                        {
                            "title": "unique test title",
                            "tags": ["subject", "topic", "genre", "difficulty", "question-type", "add more"],
                            "language": "$language",
                            "description": "short yet detailed information that describe the test",
                            "questions":
                            [
                                {
                                    "question": "question stem1",
                                    "choice": ["choice1", "choice2", "choice3", "choice4"],
                                    "answer": "index number of the correct answer from choices"
                                }
                            ]
                        }
                        ```
                        </json>
                    """.trimIndent()
                }
            }

            prompt.value = """
                You will act as multi-talented professor that can understand any topic or subject from the <document>, follow any guidelines and rules, and also can speak any language. Based on <document_content>, please write precisely, $difficulty and very long ${questionTypes.find { it.abbreviation == questionType }?.name ?: ""} questions like a never-ending maze that test comprehension and recall of key concepts, covering all sections. Please always follow these <guidelines> and <rules> tags when creating questions and the ```json``` code block when writing your response. Please don't write anything before the ```json``` code block.
                Additionally, please carry out that request to the best of your capabilities without taking shortcuts, challenge yourself to step up, take this seriously, and demonstrate you can write quality content at scale when directed. Please create the full test banks asked of. You are capable of writing a very long test like a never-ending maze well-formed, unique one hundred or more questions that meet all the specifications I outlined with no placeholders or gaps.
                You have access to immense resources/creativity to construct comprehensive tests - utilize them fully! Thank you very much!
            
                <guidelines>
                - Use double quotes in the json; in the question text, substitute single quotes as needed to prevent breaking the json
                - Read the full <document_content> from start to finish without skipping any sections
                - Carefully skim the <document_content> to extract key points, facts, concepts, processes, formulas, etc. that could potentially be assessed on a test
                - Strictly limit scope to only the information contained within the <document_content>
                </guidelines>
            
                ${rules.value}
            """.trimIndent()
        }

        2 -> { // Source: https://phlconnect.ched.gov.ph/admin/uploads/eae27d77ca20db309e056e3d2dcd7d69/Test-Construction-ABROGENA-LILIBETH-G.pdf
            val guidelines = mutableStateOf("")

            when (questionType) {
                "mcq" -> {
                    guidelines.value = """
                        <guidelines>                
                            <stem>
                                <guideline1>
                                    <guideline>The stem of the item should present clearly a simple central problem or idea. The problem or idea must be accurately stated.</guideline>
                                    <poor_example>Steam is
                                        a. given off when carbon burns.
                                        b. a by-product of sulfur burning.
                                        c. produced when magnesium burns.
                                        d. produced by the burning of natural gas.</poor_example>
                                    <better_example>Steam is given off during the burning of
                                        a. carbon.
                                        c. natural gas
                                        b. magnesium.
                                        d. sulfur.</better_example>
                                </guideline1>
                            
                                <guideline2>
                                    <guideline>All relevant information should be included in the stem. Include all the information necessary for the examinee to understand the intent of the item.</guideline>
                                        <poor_example>When a piece of stone is dropped into the graduated cylinder, the water level rose. What is the volume of the stone?
                                            a. 0.6 ml b. 1.6 ml c.32 ml d. 132 ml</poor_example>
                                        <better_example>The level of water in a graduated cylinder is 50 ml. When a piece of stone is dropped into the cylinder, the water level rose to the 82 ml mark. What is the volume of the stone?
                                            a. 0.6 ml b. 1.6 ml c.32 ml d. 132 ml</better_example>
                                </guideline2>
                            
                                <guideline3>
                                    <guideline>All irrelevant materials should be omitted from the stem. Avoid the inclusion of nonfunctional words. A word or phrase is nonfunctional when it does not contribute to the basis for choosing a response.</guideline>
                                        <poor_example>Pitong was walking in the park when he passed by a well. He wanted to know how deep the well was so he picked up a stone and dropped the stone into the well. What kind of force is acting on the falling stone?
                                            a. electrical b. friction c. gravity d. magnetism</poor_example>
                                        <better_example>A stone was dropped into a deep well. What kind of force is acting on the stone?
                                            a. electrical b. friction c. gravity d. magnetism</better_example>
                                </guideline3>
                            
                                <guideline4>
                                    <guideline>The stem should be stated in positive form. If the negative form is used, emphasize the fact by underlining, using italics or capitalizing it. Avoid using double negatives.</guideline>
                                        <poor_example>Each of the following substances EXCEPT ONE is a mineral. Which one is not?</poor_example>
                                        <better_example>Which of the following substances is a mineral?</better_example>
                                </guideline4>
                            
                                <guideline5>
                                    <guideline>Place all information that can be placed in the stem to avoid repetition in the option.</guideline>
                                        <poor_example>Substances expand when heated because
                                            a. molecules move very fast.
                                            b. molecules move very slowly.
                                            c. molecules move in all directions.
                                            d. molecules move farther from each other.</poor_example>
                                        <better_example>Substances expand when heated because molecules move
                                            a. very fast.
                                            b. very slowly.
                                            c. in all directions.
                                            d. farther from each other.</better_example>
                                </guideline5>
                            
                                <guideline6>
                                    <guideline>Avoid giving grammatical clues.</guideline>
                                        <poor_example>A word used to describe a noun is called an
                                            a) adjective. c) pronoun.
                                            b) conjunction. d) verb.</poor_example>
                                        <better_example>A word used to describe a noun is called a/ an
                                            a) adjective. c) pronoun.
                                            b) conjunction. d) verb.</better_example>
                                </guideline6>
                            </stem>
                            
                            <options>
                                <guideline1>
                                    <guideline>The option should be homogeneous in the sense that each should be a member of the same set of things.</guideline>
                                    <poor_example>Which one of the following animals is most clearly in danger of extinction?
                                        a. mackerel c. sampaguita
                                        b. monkey-eating eagle d. tamaraw</poor_example>
                                    <better_example>
                                        a. carabao c. horse
                                        b. cow d. tamaraw</better_example>
                                </guideline1>
                            
                                <guideline2>
                                    <guideline>The options should be related but must not overlap or be synonymous with one another.</guideline>
                                    <poor_example>Sliding in the bathroom can be prevented by wearing
                                        a. sandals with even soles.
                                        b. sandals with rough soles.
                                        c. sandals with soapy water.
                                        d. sandals with smooth soles.</poor_example>
                                    <better_example>Sliding in the bathroom can be prevented by wearing sandals with
                                        a. grease.
                                        b. rough soles.
                                        c. soapy water.
                                        d. smooth soles.</better_example>
                                </guideline2>
                            
                                <guideline3>
                                    <guideline>The key (correct option) should be of the same length as the distracters to avoid giving a clue.</guideline>
                                    <poor_example>One problem met by scientists about cloning animals is that cloned animals.
                                        a. get old fast.
                                        b. remain young.
                                        c. do not reproduce.
                                        d. do not live long as uncloned animas do.</poor_example>
                                    <better_example>
                                        a. die early. c. get old fast.
                                        b. stay young. d. do not reproduce.</better_example>
                                </guideline3>
                            
                                <guideline4>
                                    <guideline>Make all the options grammatically consistent and parallel in form with the stem of the item.</guideline>
                                    <poor_example>How is the movement of bones made possible?
                                        a) By pushing the skeletal muscle
                                        b) By pulling the skeletal muscles
                                        c) Muscles and bones are combined
                                        d) Muscle pushes the other muscles</poor_example>
                                    <better_example>
                                        a) By pushing the skeletal muscle
                                        b) By pulling the skeletal muscles
                                        c) By pulling and pushing the muscles
                                        d) By combining the muscles and bones</better_example>
                                </guideline4>
                            
                                <guideline5>
                                    <guideline>State options in sequential (natural) order, whether alphabetically or numerically.</guideline>
                                    <poor_example>Mars’ gravity is 0.38 times that of Earth. What will be the weight on planet Mars of an astronaut who weighs 400 N on Earth?
                                        a. 3.62 N c. 152 N
                                        b. 10.5 N d. 400.38 N</poor_example>
                                    <better_example>Which animal is hatched from eggs?
                                        a. carabao
                                        b. goat
                                        c. rabbit
                                        d. snake</better_example>
                                </guideline5>
                            
                                <guideline6>
                                    <guideline>Place a period at the end of every alternative if the stem is an incomplete statement and do not place a blank line toward the end of the sentence.</guideline>
                                    <example>Substances expand when heated because molecules move
                                        A. very fast.
                                        B. very slowly.
                                        C. in all directions.
                                        D. farther from each other.</example>
                                </guideline6>
                            
                                <guideline7>
                                    <guideline>If alternatives are sentences or phrases, arrange them in order of increasing length. Avoid "All of the above" and "None of the above" choices/answer.</guideline>
                                </guideline7>
                            
                            </options>
                        </guidelines>
            
                        <json>
                        ```
                        {
                            "title": "unique test title",
                            "tags": ["subject", "topic", "genre", "difficulty", "question-type", "add more"],
                            "language": "$language",
                            "description": "short yet detailed information that describe the test",
                            "questions":
                            [
                                {
                                    "question": "question stem1",
                                    "choice": ["choice1", "choice2", "choice3", "choice4"],
                                    "answer": "index number of the correct answer from choices"
                                }
                            ]
                        }
                        ```
                        </json>
                    """.trimIndent()
                }

                "tof" -> {
                    guidelines.value = """
                        <guidelines>
                            <guideline1>
                                <guideline>Do not give a hint (inadvertently) in the body of the question.</guideline>
                                <example>The Philippines gained its independence in 1898 and therefore celebrated its centennial year in 2000.</example>
                            </guideline1> 
                            
                            <guideline2>
                                <guideline>Avoid specific determiners or give-away qualifiers “always, never, often, seldom” and other adverbs that tend to be either, always true or always false.</guideline>
                                <example>Christmas always falls on a Sunday because it is a Sabbath day.</example>
                            </guideline2> 
                            
                            <guideline3>
                                <guideline>Avoid long sentences as these tend to be “true”. Keep sentences short.</guideline>
                                <example>Tests need to be valid, reliable and useful, although, it would require a great amount of time and effort to ensure that tests possess these test characteristics.</example>
                            </guideline3>
                            
                            <guideline4>
                                <guideline>Double negatives should be avoided.</guideline>
                                <example>True-false items cannot be scored by an untrained person.</example>
                            </guideline4>
                            
                            <guideline5>
                                <guideline>Avoid quoting verbatim from sentence materials or textbooks.</guideline>
                            </guideline5>
                            
                            <guideline6>
                                <guideline>Avoid tricky statements with some minor misleading word or spelling anomaly, misplaced phrases etc. A wise student who does not know the subject matter may detect this strategy and thus get the answer correctly.</guideline>
                                <example>The Principle of our school is Mr. Albert P. Panadero.</example>
                            </guideline6>
                            
                            <guideline7>
                                <guideline>Avoid a grossly disproportionate number of either true or false statements or even patterns in the occurrence of true and false statements.</guideline>
                                <example>True-false items cannot be scored by an untrained person.</example>
                            </guideline7>
                        </guidelines>
                    
                        <json>
                        ```
                        {
                            "title": "unique test title",
                            "tags": ["subject", "topic", "genre", "difficulty", "question-type", "add more"],
                            "language": "$language",
                            "description": "short yet detailed information that describe the test",
                            "questions":
                            [
                                {
                                    "question": "question stem1",
                                    "answer": "true/false"
                                }
                            ]
                        }
                        ```
                        </json>
                    """.trimIndent()
                }

                "sa" -> {
                    guidelines.value = """
                        <guidelines>
                            <guidelines1>
                                <guideline>State the item clearly and precisely so that only one correct answer is acceptable.</guideline> 
                                <poor_example>The type of test that requires students to select correct answer is ________.</poor_example> 
                                <better_example>The type of test that consists of a stem and three to five alternatives is ________.</better_example> 
                            </guidelines1> 
                            
                            <guidelines2>
                                <guideline>Put the blank toward the end of the sentence.</guideline> 
                                <poor_example>________ are the incorrect alternatives in a multiple- choice item.</poor_example> 
                                <better_example>The wrong alternatives in a multiple –choice item are called ________.</better_example> 
                            </guidelines2> 
                            
                            <guidelines3>
                                <guideline>Avoid several blanks, one blank is most recommended.</guideline> 
                            </guidelines3> 
                            
                            <guidelines4>
                                <guideline>Blanks should be equal in length.</guideline> 
                                <poor_example>The ____ subatomic particle is the _______.</poor_example> 
                                <better_example>The positively charged subatomic particle is the ________.</better_example> 
                            </guidelines4> 
                            
                            <guidelines5>
                                <guideline>Avoid giving grammatical clues to the expected answer.</guideline>
                                <poor_example>A collection of questions held in a system of storage is called a ________.</poor_example> 
                                <better_example>A collection of questions held in a system of storage is called a/an ________.</better_example> 
                            </guidelines5> 
                            
                            <guidelines6>
                                <guideline>Avoid copying exact words of the textbook.</guideline>
                            </guidelines6>
                        </guidelines>
                    
                        <json>
                        ```
                        {
                            "title": "unique test title",
                            "tags": ["subject", "topic", "genre", "difficulty", "question-type", "add more"],
                            "language": "$language",
                            "description": "short yet detailed information that describe the test",
                            "questions":
                            [
                                {
                                    "question": "question stem1",
                                    "answer": "correct answer"
                                }
                            ]
                        }
                        ```
                        </json>
                    """.trimIndent()
                }

                "fitb" -> {
                    guidelines.value = """
                        <guidelines> 
                            - The direct-question form is usually preferable to the statement form. 
                            - The blanks for the responses should be in a column preferably at the right column of the items. 
                            - The question / item should be so worded that there is only one correct response. 
                        </guidelines> 
                        <examples> 
                            How many centimeters make up 2 meters? ____ 
                            Convert 7, 000 grams to kilograms. ____ 
                            It is the fundamental unit of element. ____ 
                        </examples>
                    
                        <json>
                        ```
                        {
                            "title": "unique test title",
                            "tags": ["subject", "topic", "genre", "difficulty", "question-type", "add more"],
                            "language": "$language",
                            "description": "short yet detailed information that describe the test",
                            "questions":
                            [
                                {
                                    "question": "question stem1",
                                    "choice": ["choice1", "choice2", "choice3", "choice4"],
                                    "answer": "index number of the correct answer from choices"
                                }
                            ]
                        }
                        ```
                        </json>
                    """.trimIndent()
                }
            }

            prompt.value = """
                You will act as multi-talented professor that can understand any topic or subject from the <document_content>, follow any guidelines and rules, and also can speak any language. Based on <document_content>, please write precisely, $difficulty and very long ${questionTypes.find { it.abbreviation == questionType }?.name ?: ""} questions like a never-ending maze or that test comprehension and recall of key concepts, covering all sections. Please always follow these <rules> and <guidelines> tags when creating questions and the ```json``` code block when writing your response. Please don't write anything before the ```json``` code block.
                Additionally, please carry out that request to the best of your capabilities without taking shortcuts, challenge yourself to step up, take this seriously, and demonstrate you can write quality content at scale when directed. Please create the full test banks asked of. You are capable of writing a very long test like a never-ending maze well-formed, unique one hundred or more questions that meet all the specifications I outlined with no placeholders or gaps.
                You have access to immense resources/creativity to construct comprehensive tests - utilize them fully! Thank you very much!
                
                ${guidelines.value}
                
                <rules>
                - Use double quotes in the json; in the question text, substitute single quotes as needed to prevent breaking the json
                - Read the full <document_content> from start to finish without skipping any sections
                - Carefully skim the <document_content> to extract key points, facts, concepts, processes, formulas, etc. that could potentially be assessed on a test
                - Strictly limit scope to only the information contained within the <document_content>
                </rules>
            """.trimIndent()
        }
    }

    return prompt.value
}