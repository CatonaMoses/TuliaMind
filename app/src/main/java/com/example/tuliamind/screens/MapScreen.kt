package com.example.tuliamind.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tuliamind.*
import com.example.tuliamind.ui.theme.Carbon
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.Marker
import java.io.File

private data class Place(val name: String, val about: String, val lat: Double, val lng: Double)

/**
 * MapView using OpenStreetMap via osmdroid — no API key required.
 * Positions are approximate; verify in Google Maps before relying on them.
 */
@Composable
fun MapScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current

    val places = remember {
        listOf(
            Place("Kenyatta National Hospital",                    "Nairobi. Referral hospital with a mental health unit.",    -1.3011,  36.8069),
            Place("Mathari National Teaching and Referral Hospital","Nairobi. Specialist mental health hospital.",             -1.2578,  36.8508),
            Place("Aga Khan University Hospital",                  "Nairobi. Private hospital with counselling services.",    -1.2634,  36.8140),
            Place("Coast General Teaching and Referral Hospital",  "Mombasa. Public referral hospital.",                     -4.0519,  39.6682),
            Place("Jaramogi Oginga Odinga Teaching and Referral",  "Kisumu. Public referral hospital.",                      -0.0993,  34.7554),
            Place("Moi Teaching and Referral Hospital",            "Eldoret. Public referral hospital.",                      0.5164,  35.2735)
        )
    }
    val cities = remember {
        listOf(
            "Nairobi"  to GeoPoint(-1.2921, 36.8219),
            "Mombasa"  to GeoPoint(-4.0435, 39.6682),
            "Kisumu"   to GeoPoint(-0.0917, 34.7680),
            "Eldoret"  to GeoPoint(0.5143,  35.2698)
        )
    }
    var selected by remember { mutableStateOf(places[0]) }
    var city     by remember { mutableStateOf("Nairobi") }

    val map = remember {
        Configuration.getInstance().let { cfg ->
            cfg.load(ctx, ctx.getSharedPreferences("osmdroid", android.content.Context.MODE_PRIVATE))
            cfg.userAgentValue = ctx.packageName
            cfg.osmdroidBasePath = File(ctx.cacheDir, "osmdroid")
            cfg.osmdroidTileCache = File(ctx.cacheDir, "osmdroid/tiles")
        }
        MapView(ctx).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(12.0)
            controller.setCenter(cities[0].second)
            overlays.add(CopyrightOverlay(ctx))
            places.forEach { p ->
                val marker = Marker(this)
                marker.position = GeoPoint(p.lat, p.lng)
                marker.title   = p.name
                marker.snippet = p.about
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.setOnMarkerClickListener { m, _ -> selected = p; m.showInfoWindow(); true }
                overlays.add(marker)
            }
        }
    }

    DisposableEffect(Unit) {
        map.onResume()
        onDispose { map.onPause(); map.onDetach() }
    }

    Column(Modifier.fillMaxSize().background(Carbon.Gray100)) {
        TopBar("Clinic map", onBack)

        Column(Modifier.padding(12.dp)) {
            Tag("MapView", "org.osmdroid.views.MapView", Carbon.Green60)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                cities.forEach { (label, point) ->
                    Box(
                        Modifier
                            .weight(1f)
                            .background(
                                if (city == label) Carbon.Blue60 else Carbon.Gray80,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable {
                                city = label
                                map.controller.setZoom(12.0)
                                map.controller.animateTo(point)
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(label, color = Color.White, fontSize = 13.sp) }
                }
            }
        }

        Box(Modifier.weight(1f).fillMaxWidth()) {
            AndroidView(factory = { map }, modifier = Modifier.fillMaxSize())
        }

        Column(Modifier.padding(12.dp).navigationBarsPadding()) {
            CarbonCard(Carbon.Green60) {
                Text(selected.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(selected.about, color = Carbon.Gray30, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                CarbonButton("Get directions", Carbon.Green60) {
                    val geo = Uri.parse(
                        "geo:${selected.lat},${selected.lng}?q=${selected.lat},${selected.lng}" +
                            "(${Uri.encode(selected.name)})"
                    )
                    safeStart(ctx, Intent(Intent.ACTION_VIEW, geo))
                }
            }
        }
    }
}
