package com.kristianskokars.tasky.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.kristianskokars.tasky.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val interFontName = GoogleFont("Inter")
val Inter = FontFamily(
    Font(googleFont = interFontName, fontProvider = provider),
    Font(googleFont = interFontName, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = interFontName, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = interFontName, fontProvider = provider, weight = FontWeight.Light),
    Font(R.font.inter)
)

val BannerHeadingStyle = TextStyle(
    color = White,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 16.sp,
        color = Black
    )
)
