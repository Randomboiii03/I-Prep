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
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
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
import com.example.i_prep.common.geminiAI
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

            HorizontalDivider()

            InfoSection(
                title = "Can I create tests with different types of questions?",
                info = "\nUsers may select different types of tests that are: multiple choice, true or false or fill-in-the-blanks. We will add more question types soon if needed."
            )

            HorizontalDivider()

            InfoSection(
                title = "What type of file can I select to generate a test from?",
                info = "\nUsers can select PDF or TXT as of now. Remember that generated test will only be text-based, so it can't read image-based text that's why we don't support PPT yet."
            )

            HorizontalDivider()

            InfoSection(
                title = "Can I make tests in different categories, topics or subjects?",
                info = "\nYes, as long as you select the necessary files you want to create a tests, AI will do the rest of the work for you."
            )

            HorizontalDivider()

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

            HorizontalDivider()

            InfoSectionWithIcon(
                title = "How do I share a test I've already made?",
                info = buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append("\n1. Click the test you want to share.\n")
                        append("2. In upper right of the screen, click ")
                    }

                    appendInlineContent("share", "[share]")

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append(" to share the test.\n\n")
                        append("You can choose anywhere or method you like to share, but as of now it is limited in number where you can share it.")
                    }
                },
                id = "share",
                icon = Icons.Outlined.Share
            )

            HorizontalDivider()

            InfoSection(
                title = "Is there a limit to how many quizzes I can create?",
                info = "\nThere will be no limit. But at the same time traffic of using model will be heavy as the failure of generating practice test will be."
            )

            HorizontalDivider()

            InfoSectionWithLink(
                title = "What AI does the app use?",
                info = buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append("\nThe app use Gemini 1.0 Pro of Google. To know more about this AI, click this ")
                    }

                    pushStringAnnotation(tag = "gemini", annotation = githubRepo)
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
                tag = "gemini",
                link = geminiAI
            )

            HorizontalDivider()

            InfoSection(
                title = "Why use Gemini AI instead of other existing AI model?",
                info = "\nGemini has a free version where developer and non-developer can explore and use. It may lose in some aspect from other AI model, it is pretty good for free of charge model.\n" +
                        "It can still compete on other existing model, but soon will be more advanced as the AI race is just started."
            )

            HorizontalDivider()

            InfoSection(
                title = "Why does creating test fails sometimes?",
                info = "\n1. Internet fluctuation or too slow\n\n" +
                        "- The connection between the app and the server of the AI has been cut or timed out due to long broken connection.\n\n" +
                        "2. Converting to JSON\n\n" +
                        "- Sometimes the response of the AI is cut or broken in which we tried to filter and fix, but it is low occurrence.\n\n" +
                        "3. AI model stop generating\n\n" +
                        "- Due to the high demand of this AI, there will be a lot of request everytime, so expect frequent failure.\n\n" +
                        "4. Bug\n\n" +
                        "- The developer of this app has not enough knowledge in this particular field, some of the error can't be patch or find to fix as of now.\n\n" +
                        "We our open for collaboration to improve this app, our contact is in About > Github."
            )

            HorizontalDivider()

            InfoSection(
                title = "Why some of the questions on the test is not in the scope of my reference file I upload?",
                info = "\nIn some cases the AI will add some related questions because the content of the file is too short to create questions."
            )

            HorizontalDivider()

            InfoSection(
                title = "Why some of the questions on the test is repeating?",
                info = "\nIn some cases the AI will repeat questions because the content of the file is too short to create questions."
            )

            HorizontalDivider()

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