package com.mak.pocketnotes.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.mak.pocketnotes.android.R


val bodyFontFamily = FontFamily(
//    Font(R.font.satoshi_black, FontWeight.Black, FontStyle.Normal),
//    Font(R.font.satoshi_black_italic, FontWeight.Black, FontStyle.Italic),
//    Font(R.font.satoshi_bold, FontWeight.Bold, FontStyle.Normal),
//    Font(R.font.satoshi_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.satoshi_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.satoshi_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.satoshi_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.satoshi_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.satoshi_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.satoshi_regular, FontWeight.Normal, FontStyle.Normal),
)

val displayFontFamily = FontFamily(
    Font(R.font.cabinet_grotesk_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.cabinet_grotesk_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.cabinet_grotesk_extrabold, FontWeight.ExtraBold, FontStyle.Normal),
//    Font(R.font.cabinet_grotesk_extralight, FontWeight.ExtraLight, FontStyle.Normal),
//    Font(R.font.cabinet_grotesk_light, FontWeight.Light, FontStyle.Normal),
//    Font(R.font.cabinet_grotesk_medium, FontWeight.Medium, FontStyle.Normal),
//    Font(R.font.cabinet_grotesk_thin, FontWeight.Thin, FontStyle.Normal),
//    Font(R.font.cabinet_grotesk_regular, FontWeight.Normal, FontStyle.Normal)
)

val typography = typographyFromDefaults()

fun typographyFromDefaults(): Typography {
    val baseline = Typography()
    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )
}