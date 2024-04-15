package com.example.i_prep.common

import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory

val emptyPTest: PTest = PTest(
    0,
    "",
    "",
    emptyList(),
    "",
    emptyList(),
    0,
    "",
    "",
    "https://picsum.photos/400/600",
    0L,
    true,
    0
)
val emptyTHistory: THistory = THistory(0, 0, emptyList(), emptyList(), 0, 0, 0)

const val updateJSON = "https://raw.githubusercontent.com/Randomboiii03/I-Prep/master/update-changelog.json"

const val googleForm = "https://forms.gle/7Sa7fZeEf57AyhFQA"

const val geminiAI = "https://deepmind.google/technologies/gemini/#introduction"

const val about1 = """
    I-Prep is a personalized test prep application which is integrated with Gemini 1.0 Pro from Google. This SERVICE is provided at no cost and is intended for use as is but with proper and responsible use is advised.
    
    This page details our policies with the use and disclosure of affiliation with Google.
    
    If you choose to use our Service, then you agree also to the Terms of Service of using Gemini AI as Google entails. We will not collect any information upon or during use of our Service. All extracted information from documents used to create the test will not be our responsibility, all will be under the Google’ Terms of Service. """

const val about2_1 = """
    For integrating Gemini AI for our Service, may require considerable patient when generating practice test, because of limit the model has, as it is a free. The uploaded documents that have been used will not be collected as described in this privacy policy.""""
const val about2_2 = """
    Links to the Terms of Service of third-party service provides used by the app:"""

const val googleTerms = "https://ai.google.dev/terms"

const val about3 = """
    We may periodically update our Privacy Policy. Thus, you are advised to review this page periodically for any changes."""

const val about4 = """
    If you have any questions or suggestions about this Privacy Policy, do not hesitate to reach out to us on """

const val githubRepo = "https://github.com/Randomboiii03/I-Prep/issues"
