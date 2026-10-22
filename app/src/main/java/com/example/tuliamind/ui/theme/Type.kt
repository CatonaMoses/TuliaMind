package com.example.tuliamind.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// DEFAULT: the phone's normal font, so the project builds with no extra files.
// OPTIONAL IBM Plex Sans: add ibm_plex_sans_regular/medium/bold.ttf to res/font (see the guide),
// then replace the next line with:
//   val PlexSans = FontFamily(
//       Font(R.font.ibm_plex_sans_regular, FontWeight.Normal),
//       Font(R.font.ibm_plex_sans_medium, FontWeight.Medium),
//       Font(R.font.ibm_plex_sans_bold, FontWeight.Bold))
// and add: import androidx.compose.ui.text.font.Font  and  import com.example.tuliamind.R
val PlexSans: FontFamily = FontFamily.Default

val TuliaTypography = Typography(
    bodyLarge = TextStyle(fontFamily = PlexSans, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = PlexSans, fontSize = 14.sp),
    titleLarge = TextStyle(fontFamily = PlexSans, fontWeight = FontWeight.Bold, fontSize = 22.sp),
    labelLarge = TextStyle(fontFamily = PlexSans, fontWeight = FontWeight.Medium, fontSize = 14.sp)
)
