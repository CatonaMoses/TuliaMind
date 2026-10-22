package com.example.tuliamind.screens

import android.content.Context
import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon

/** Hub that explains and demonstrates the four main Android app components. */
@Composable
fun LabScreen(onBack: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    ScreenScaffold("Android Lab", onBack) {
        Text(
            "Every Android app is built from four components: Activities, Services, " +
                "Broadcast Receivers and Content Providers. Intents start them and the " +
                "Manifest declares them. Try each one below.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(16.dp))
        ActivityDemo(ctx);  Spacer(Modifier.height(16.dp))
        IntentsDemo(ctx);   Spacer(Modifier.height(16.dp))
        ServiceDemo(ctx);   Spacer(Modifier.height(16.dp))
        BroadcastDemo(ctx); Spacer(Modifier.height(16.dp))
        ProviderDemo(ctx);  Spacer(Modifier.height(16.dp))
        ManifestDemo(ctx);  Spacer(Modifier.height(16.dp))
        ResourcesDemo()
        Disclaimer()
    }
}

/** Reads the app's own AndroidManifest.xml at run time and lists what it declares. */
@Composable
fun ManifestDemo(ctx: Context) {
    val info = remember {
        try {
            ctx.packageManager.getPackageInfo(
                ctx.packageName,
                PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES or
                    PackageManager.GET_RECEIVERS or PackageManager.GET_PROVIDERS or
                    PackageManager.GET_PERMISSIONS
            )
        } catch (_: Exception) { null }
    }
    fun names(list: Array<out ComponentInfo>?) =
        list?.joinToString { it.name.substringAfterLast('.') }?.ifEmpty { "none" } ?: "none"

    CarbonCard(Carbon.Cyan50) {
        Tag("Manifest", "AndroidManifest.xml", Carbon.Cyan50)
        Text("6. The Manifest", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(
            "The Manifest is the app's ID card. Android reads it before running any code. " +
                "The values below are read live from the system at runtime.",
            color = Carbon.Gray30,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(8.dp))
        Text("Package: ${ctx.packageName}", color = Carbon.Cyan50, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        Text("Activities: ${names(info?.activities)}", color = Carbon.Gray10, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        Text("Services: ${names(info?.services)}", color = Carbon.Gray10, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        Text("Receivers: ${names(info?.receivers)}", color = Carbon.Gray10, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        Text("Providers: ${names(info?.providers)}", color = Carbon.Gray10, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        Text(
            "Permissions: " + (info?.requestedPermissions?.joinToString { it.substringAfterLast('.') } ?: "none"),
            color = Carbon.Gray10,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
    }
}

/** Other project files that are essential but are not Android components. */
@Composable
fun ResourcesDemo() {
    val items = listOf(
        "AndroidManifest.xml" to "Declares components, permissions and the launcher Activity",
        "res/values"          to "strings.xml (app name), colours and themes",
        "res/mipmap"          to "Launcher icons in several density buckets",
        "build.gradle.kts"    to "SDK versions and dependencies (Compose, Navigation, osmdroid)",
        "Jetpack Compose"     to "UI written in Kotlin — replaces XML layouts in modern apps"
    )
    CarbonCard(Carbon.Yellow30) {
        Tag("Resources", "res/  +  Gradle", Carbon.Orange60)
        Text("7. Resources and project files", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(Modifier.height(8.dp))
        items.forEach { (file, desc) ->
            Text(file, color = Carbon.Yellow30, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            Text(desc, color = Carbon.Gray10, fontSize = 13.sp)
            Spacer(Modifier.height(6.dp))
        }
    }
}
