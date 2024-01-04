package com.example.i_prep.presentation.more.composables.help

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.i_prep.common.claudeAI
import com.example.i_prep.common.githubRepo
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.more.components.MTopBar

@Composable
fun Help(globalEvent: (GlobalEvent) -> Unit, onBack: () -> Unit, modifier: Modifier = Modifier) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(false))
    }

    Scaffold(
        topBar = { MTopBar(onBack = { onBack() }, title = "Help") }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoSectionWithIcon(
                title = "How to generate a new test?",
                info = buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append("\n1. Click the ")
                    }

                    appendInlineContent("pencil", "[pencil icon]")

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append(" in bottom left middle of the screen.\n")
                        append("2. Select \"question types\" whether it's a multiple choice, true or false or fill-in the blank.\n")
                        append("3. Click \"reference file\" and select a document (e.g. PDF, DOCX or TXT) from your storage that you want to generate a test.\n")
                        append("4. Select \"language\".\n")
                        append("5. Select \"difficulty\" whether it's easy, intermediate or hard.\n")
                        append("6. Click \"Reset\" if you want the input data to be cleared or click \"Generate\" if you want to create the test.\n")
                        append("7. Lastly, enjoy taking the your generated test.")
                    }
                },
                id = "pencil",
                icon = Icons.Filled.Create
            )

            Divider()

            InfoSection(
                title = "Can I create tests with different types of questions?",
                info = "\nUsers may select different types of tests that are: multiple choice, true or false or fill-in-the-blanks. We will add more question types soon if needed."
            )

            Divider()

            InfoSection(
                title = "What type of file can I select to generate a test from?",
                info = "\nUsers can select PDF, DOCX or TXT as of now. Remember that generated test will only be text-based, so it can't read image-based text that's why we don't support PPT yet."
            )

            Divider()

            InfoSection(
                title = "Can I make tests in different categories, topics or subjects?",
                info = "\nYes, as long as you select the necessary files you want to create a tests, AI will do the rest of the work for you."
            )

            Divider()

            InfoSectionWithIcon(
                title = "How do I edit or delete a test I've already made?",
                info = buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append("\n1. Click the test you want to edit or delete.\n")
                        append("2. In upper right of the screen, click ")
                    }

                    appendInlineContent("more", "[more vertical]")

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append(" to show the edit and delete options.\n\n")
                        append("You can edit the name, number of item of the test and if the test is timed or not.")
                    }
                },
                id = "more",
                icon = Icons.Outlined.MoreVert
            )

            Divider()

            InfoSection(
                title = "Can I share my test to others?",
                info = "\nAs of now, the app can't share test for others. If the app become successful, we may add more feature like this."
            )

            Divider()

            InfoSection(
                title = "Is there a limit to how many quizzes I can create?",
                info = "\nUsers can create up to 10 every 6-10 hours or it may vary based on demand on that day. It will reset every day so user can create more."
            )

            Divider()

            InfoSectionWithLink(
                title = "What AI does the app use?",
                info = buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append("\nThe app use Claude AI of Anthropic. To know more about this AI, click this ")
                    }

                    pushStringAnnotation(tag = "claude", annotation = githubRepo)
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("link")
                    }
                    pop()

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append(".")
                    }
                },
                tag = "claude",
                link = claudeAI
            )

            Divider()

            InfoSection(
                title = "Why use Claude instead of GPT of OpenAI?",
                info = "\nClaude has a huge context window than newest model of GPT, it may lose in some aspect but it is good to use when it comes to QA of documents.\n\n" +
                        "Also, we all know that API of any AI cost money, we don't use any official API instead we use Claude web chat's API - reversed engineer it to be use of in this app. \n\n" +
                        "Is this legal? We don't know, that's why use this app at your own risk. Check our Privacy Policy for more info."
            )

            Divider()

            InfoSection(
                title = "Why does creating test fails sometimes?",
                info = "\n1. Internet fluctuation or too slow\n\n" +
                        "- The connection between the app and the server of the AI has been cut or timed out due to long broken connection.\n\n" +
                        "2. Converting to JSON\n\n" +
                        "- Sometimes the response of the AI is cut or broken in which we tried to filter and fix, but with many possibility we can't patch all of it.\n\n" +
                        "3. AI can't understand the prompt\n\n" +
                        "- Due to the length of prompt, added extracted text from the reference file or some privacy policy of the company of the Anthropic, it refused to follow the prompt that the app send.\n\n" +
                        "4. Bug\n\n" +
                        "- The developer of this app is a student with not enough knowledge in this particular field, some of the error can't be patch or find to fix.\n\n" +
                        "We our open for collaboration to improve this app, our contact is in About > Github."
            )

            Divider()

            InfoSection(
                title = "Why does the number of questions is not the same even though it is same reference file?",
                info = "\nThe AI has a set of temperature which controls the randomness of the response it gives and also, the AI is continuously learning and improving."
            )

            Divider()

            InfoSection(
                title = "Why some of the questions on the test is not in the scope of my reference file I upload?",
                info = "\nIn some cases the AI will add some related questions because the content of the file is too short to create questions."
            )

            Divider()

            InfoSection(
                title = "How do I suggest new features or report bug?",
                info = "\nYou can do it by going to About > Github."
            )
        }
    }
}

@Composable
private fun InfoSection(title: String, info: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        Text(text = info, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Justify)
    }
}

@Composable
fun InfoSectionWithLink(
    title: String,
    info: AnnotatedString,
    tag: String,
    link: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        ClickableText(
            text = info,
            style = MaterialTheme.typography.bodyLarge,
            onClick = { offSet ->
                info.getStringAnnotations(tag = tag, start = offSet, end = offSet)
                    .firstOrNull()?.let {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                    }
            }
        )
    }
}

@Composable
private fun InfoSectionWithIcon(
    title: String,
    info: AnnotatedString,
    id: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = info,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            inlineContent = mapOf(
                Pair(
                    id,
                    InlineTextContent(
                        Placeholder(
                            width = 15.sp,
                            height = 15.sp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
                        )
                    ) {
                        Icon(imageVector = icon, contentDescription = id)
                    }
                )
            )
        )
    }
}