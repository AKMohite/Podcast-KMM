package com.mak.pocketnotes.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mak.pocketnotes.android.R


internal enum class TextSize {
    SMALL, MEDIUM, LARGE, EXTRA_LARGE
}

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

fun typographyFromDefaults(fontScale: Float = 1f): Typography {
    val baseline = Typography()
    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold,      fontSize = (57 * fontScale).sp, lineHeight = (64 * fontScale).sp),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold,      fontSize = (45 * fontScale).sp, lineHeight = (52 * fontScale).sp),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily, fontWeight = FontWeight.SemiBold,  fontSize = (36 * fontScale).sp, lineHeight = (44 * fontScale).sp),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily, fontWeight = FontWeight.SemiBold,  fontSize = (32 * fontScale).sp, lineHeight = (40 * fontScale).sp),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily, fontWeight = FontWeight.SemiBold,  fontSize = (28 * fontScale).sp, lineHeight = (36 * fontScale).sp),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily, fontWeight = FontWeight.Medium,    fontSize = (24 * fontScale).sp, lineHeight = (32 * fontScale).sp),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily,          fontWeight = FontWeight.SemiBold,  fontSize = (22 * fontScale).sp, lineHeight = (28 * fontScale).sp),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily,          fontWeight = FontWeight.Medium,    fontSize = (16 * fontScale).sp, lineHeight = (24 * fontScale).sp),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily,          fontWeight = FontWeight.Medium,    fontSize = (14 * fontScale).sp, lineHeight = (20 * fontScale).sp),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily,          fontWeight = FontWeight.Normal,    fontSize = (16 * fontScale).sp, lineHeight = (24 * fontScale).sp),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily,          fontWeight = FontWeight.Normal,    fontSize = (14 * fontScale).sp, lineHeight = (20 * fontScale).sp),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily,          fontWeight = FontWeight.Normal,    fontSize = (12 * fontScale).sp, lineHeight = (16 * fontScale).sp),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily,          fontWeight = FontWeight.Medium,    fontSize = (14 * fontScale).sp, lineHeight = (20 * fontScale).sp),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily,          fontWeight = FontWeight.Medium,    fontSize = (12 * fontScale).sp, lineHeight = (16 * fontScale).sp),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily,          fontWeight = FontWeight.Medium,    fontSize = (11 * fontScale).sp, lineHeight = (16 * fontScale).sp),
    )
}