package com.example.i_prep.presentation.create.composables.webview

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.i_prep.common.claudeChatUrl
import com.example.i_prep.common.claudeSentryUrl
import com.example.i_prep.common.userAgent
import com.example.i_prep.presentation.GlobalEvent
import com.example.i_prep.presentation.create.CEvent
import com.example.i_prep.presentation.create.CViewModel
import com.example.i_prep.presentation.create.composables.generate.GEvent
import com.example.i_prep.presentation.create.model.CreateNav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Webview(
    cEvent: (CEvent) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    globalEvent: (GlobalEvent) -> Unit
) {
    LaunchedEffect(true) {
        globalEvent(GlobalEvent.ShowBottomNav(false))
    }

    val mWTViewModel = viewModel<WViewModel>()
    val onEvent = mWTViewModel::onEvent
    val state by mWTViewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    var sentryUrl = ""

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

                                try {
                                    if (url != null) {
                                        if (url.contains(claudeSentryUrl, ignoreCase = true)) {
                                            sentryUrl = url
                                        }

                                        if (view != null) {
                                            val tempUrl = view.url
                                            Log.v("TAG - progress", view.progress.toString())

                                            if (tempUrl != null && view.progress == 100 && !state.status) {

                                                if (tempUrl.contains(
                                                        "claude.ai/login",
                                                        ignoreCase = true
                                                    ) ||
                                                    tempUrl.contains(
                                                        "claude.ai/onboarding",
                                                        ignoreCase = true
                                                    )
                                                ) {
                                                    onEvent(WEvent.SetLoading(false))
                                                } else if (tempUrl.contains(
                                                        claudeChatUrl,
                                                        ignoreCase = true
                                                    )
                                                ) {
                                                    val tempCookie =
                                                        cookieManager.getCookie(sentryUrl)
                                                    val cookie = tempCookie.split(' ')
                                                        .filterNot { it.contains("__stripe", true) }
                                                        .joinToString(" ")

                                                    onEvent(WEvent.SetLoading(true))
                                                    onEvent(WEvent.SetStatus(true))
                                                    cEvent(CEvent.SetCookie(cookie = cookie))

                                                    navHostController.navigate(CreateNav.Generate.title) {
                                                        popUpTo(CreateNav.Form.title)
                                                    }

                                                } else {
                                                    view.loadUrl("https://claude.ai/login")
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Webview taking too long to load. Please try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navHostController.popBackStack()
                                }
                            }
                        }
                    }
                }
            }, update = {
                it.clearCache(true)
                it.loadUrl("https://claude.ai/login")
            })
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
                        true -> ""
                        false -> "Checking if logged in on Claude AI..."
                    },
                    textAlign = TextAlign.Center,
                    modifier = modifier.padding(top = 6.dp)
                )
            }
        }

        if (state.showDialog) {
            AlertDialog(
                onDismissRequest = { onEvent(WEvent.ShowDialog(false)) },
                confirmButton = {
                    Button(
                        onClick = {
                            cEvent(CEvent.Reset)
                            onEvent(WEvent.ShowDialog(false))
                            navHostController.popBackStack()
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { onEvent(WEvent.ShowDialog(false)) }) {
                        Text(text = "Cancel")
                    }
                },
                title = { Text(text = "Cancel") },
                text = { Text(text = "Are you sure you want to cancel the process?") })
        }
    }
}