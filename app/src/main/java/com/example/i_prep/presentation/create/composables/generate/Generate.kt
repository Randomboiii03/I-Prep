package com.example.i_prep.presentation.create.composables.generate

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.i_prep.common.NotificationService
import com.example.i_prep.common.claudeChatUrl
import com.example.i_prep.common.claudeSentryUrl
import com.example.i_prep.common.googleCPUrl
import com.example.i_prep.common.userAgent
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.create.CEvent
import com.example.i_prep.presentation.create.CViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Generate(
    mCViewModel: CViewModel,
    cEvent: KFunction1<CEvent, Unit>,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    globalEvent: (GlobalEvent) -> Unit
) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(false))
    }

    val mGTViewModel = viewModel<GViewModel>()
    val onEvent = mGTViewModel::onEvent
    val state by mGTViewModel.state.collectAsState()

    val cState by mCViewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val notification = NotificationService(LocalContext.current)

    Scaffold(
        bottomBar = {
            if (!state.isLoading) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "If Google Login is not working.\nPlease try other login method.",
                        actionLabel = "Got it",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        if (state.status != "Create" || state.status != "Success") {
            Box(
                modifier = modifier
                    .statusBarsPadding()
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(factory = {
                    WebView(it).apply {
                        CoroutineScope(Dispatchers.Main).launch {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            val cookieManager = CookieManager.getInstance()

                            settings.javaScriptEnabled = true
                            settings.javaScriptCanOpenWindowsAutomatically = true
                            settings.cacheMode = WebSettings.LOAD_NO_CACHE

                            settings.userAgentString = userAgent

                            webViewClient = object : WebViewClient() {
                                override fun onLoadResource(view: WebView?, url: String?) {
                                    super.onLoadResource(view, url)

                                    cookieManager.setAcceptThirdPartyCookies(view, true)
                                    cookieManager.setAcceptCookie(true)

                                    if (url != null) {
                                        if (url.contains(claudeSentryUrl, ignoreCase = true)) {
                                            val cookie = cookieManager.getCookie(url)

                                            if (cookie.contains("sessionKey")) {
                                                if (state.status != "Create") {
                                                    onEvent(GEvent.SetState("Success", true))
                                                    onEvent(
                                                        GEvent.GenerateTest(
                                                            state = cState,
                                                            cookie = cookie,
                                                            globalEvent = globalEvent,
                                                            notification = notification,
                                                            navHostController = navHostController
                                                        )
                                                    )
                                                }
                                            }
                                        } else if (url.contains(googleCPUrl, ignoreCase = true)) {
                                            scope.launch {
                                                if (state.status != "Create") {
                                                    onEvent(GEvent.SetState("Failed", true))
                                                    delay(3500)
                                                    onEvent(GEvent.SetState("Failed", false))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, update = {
                    it.loadUrl(claudeChatUrl)
                })
            }
        }

        if (state.isLoading) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(
                    text = when (state.status) {
                        "Create" -> "Creating test.\nPlease wait atleast 3-5 minutes"
                        "Failed" -> "Failed to connect.\nPlease login to Claude AI"
                        "Success" -> "Connected successfully to Claude AI"
                        else -> "Connecting to Claude AI"
                    },
                    textAlign = TextAlign.Center
                )
            }
        }

        if (state.showDialog) {
            AlertDialog(
                onDismissRequest = { onEvent(GEvent.ShowDialog(false)) },
                confirmButton = {
                    Button(
                        onClick = {
                            cEvent(CEvent.Reset)
                            onEvent(GEvent.ShowDialog(false))
                            navHostController.popBackStack()
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { onEvent(GEvent.ShowDialog(false)) }) {
                        Text(text = "Cancel")
                    }
                },
                title = { Text(text = "Cancel") },
                text = { Text(text = "Are you sure you want to cancel the process?") })
        }
    }
}