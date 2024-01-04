package com.example.i_prep.presentation.more.composables.about.composables

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.i_prep.common.about1
import com.example.i_prep.common.about2_1
import com.example.i_prep.common.about2_2
import com.example.i_prep.common.about3
import com.example.i_prep.common.about4
import com.example.i_prep.common.claudePrivPol
import com.example.i_prep.common.githubRepo
import com.example.i_prep.presentation.more.components.MTopBar

@Composable
fun PrivacyPolicy(onBack: () -> Unit, modifier: Modifier = Modifier) {

    Scaffold(
        topBar = { MTopBar(onBack = { onBack() }, title = "Privacy Policy") }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoSection(title = "Introduction", info = about1)

            Divider()

            InfoSectionWithLink(
                title = "Use of Personal Account and Use",
                annotatedString = buildAnnotatedString {

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append(about2_1 + "\n")

                        append(about2_2 + "\n\n• ")
                    }

                    pushStringAnnotation(tag = "anthropic", annotation = claudePrivPol)
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Anthropic")
                    }
                    pop()
                },
                tag = "anthropic",
                link = claudePrivPol
            )

            Divider()

            InfoSection(title = "Changes to This Privacy Policy", info = about3)

            Divider()

            InfoSectionWithLink(
                title = "Contact Us",
                annotatedString = buildAnnotatedString {

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append(about4)
                    }

                    pushStringAnnotation(tag = "github", annotation = githubRepo)
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Github")
                    }
                    pop()

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                        append(".")
                    }
                },
                tag = "github",
                link = githubRepo
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
    annotatedString: AnnotatedString,
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
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge,
            onClick = { offSet ->
                annotatedString.getStringAnnotations(tag = tag, start = offSet, end = offSet)
                    .firstOrNull()?.let {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                    }
            }
        )
    }
}