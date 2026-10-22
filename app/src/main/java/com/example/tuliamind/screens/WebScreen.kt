package com.example.tuliamind.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

private const val WHO_URL     = "https://www.who.int/health-topics/mental-health"
private const val MOH_URL     = "https://www.health.go.ke"
private const val OFFLINE_HTML = """
<html><body style="background:#161616;color:#f4f4f4;font-family:sans-serif;padding:20px;line-height:1.6">
<h2 style="color:#78a9ff">Offline wellbeing tips</h2>
<p>This page is stored inside the app and works without internet.</p>
<ul>
<li>Sleep 7–9 hours and keep a regular routine.</li>
<li>Move your body for 10 minutes a day.</li>
<li>Talk to one trusted person when things feel heavy.</li>
<li>Limit alcohol, miraa and cannabis — they make low mood worse.</li>
<li>Ask for help early. Call 1199 (Kenya Red Cross) if you are in distress.</li>
</ul>
</body></html>
"""

/** WebView — shows web pages inside the app (android.webkit.WebView). */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    var address by remember { mutableStateOf(WHO_URL) }
    var loading by remember { mutableStateOf(false) }
    var failed  by remember { mutableStateOf(false) }

    val web = remember {
        WebView(ctx).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    loading = true; failed = false
                    if (url != null && url.startsWith("http")) address = url
                }
                override fun onPageFinished(view: WebView?, url: String?) { loading = false }
                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    if (request?.isForMainFrame == true) failed = true
                }
            }
            loadUrl(WHO_URL)
        }
    }

    DisposableEffect(Unit) { onDispose { web.destroy() } }
    BackHandler { if (web.canGoBack()) web.goBack() else onBack() }

    fun go(raw: String) {
        val u = raw.trim()
        if (u.isNotEmpty()) web.loadUrl(if (u.startsWith("http")) u else "https://$u")
    }

    Column(Modifier.fillMaxSize().background(Carbon.Gray100)) {
        TopBar("Learn", onBack)
        Column(Modifier.padding(12.dp)) {
            Tag("WebView", "android.webkit.WebView", Carbon.Cyan50)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f)) {
                    CarbonField(address, { address = it }, "Web address")
                }
                Spacer(Modifier.width(8.dp))
                CarbonButton("Go", Carbon.Blue60, Modifier.width(68.dp)) { go(address) }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    "WHO"          to { go(WHO_URL) },
                    "Kenya MoH"    to { go(MOH_URL) },
                    "Offline tips" to { web.loadDataWithBaseURL(null, OFFLINE_HTML, "text/html", "UTF-8", null) },
                    "Reload"       to { web.reload() }
                ).forEach { (label, action) ->
                    Box(
                        Modifier
                            .weight(1f)
                            .background(Carbon.Gray80, RoundedCornerShape(6.dp))
                            .clickable { action() }
                            .padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(label, color = Color.White, fontSize = 12.sp) }
                }
            }
            if (loading) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(16.dp), color = Carbon.Cyan50, strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Loading…", color = Carbon.Gray30, fontSize = 12.sp)
                }
            }
            if (failed) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Could not load the page. Check your internet, or open Offline tips.",
                    color = Carbon.Red60,
                    fontSize = 13.sp
                )
            }
        }
        AndroidView(
            factory = { web },
            modifier = Modifier.weight(1f).fillMaxWidth().navigationBarsPadding()
        )
    }
}
