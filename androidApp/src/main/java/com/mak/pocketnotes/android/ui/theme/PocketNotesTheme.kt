package com.mak.pocketnotes.android.ui.theme

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import app.mak.pocketnotes.core.designsystem.theme.roundShapes
import app.mak.pocketnotes.core.designsystem.theme.typographyFromDefaults
import com.mak.pocketnotes.domain.models.AppTheme

@Composable
fun PocketNotesTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    fontScale: Float = 1f,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> selectSchemeForContrast(isDark = darkTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typographyFromDefaults(fontScale),
        shapes = roundShapes,
        content = content
    )
}

private fun isContrastAvailable(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
}

@Composable
private fun selectSchemeForContrast(isDark: Boolean): ColorScheme {
    val context = LocalContext.current
    var colorScheme = if (isDark) darkScheme else lightScheme
    val isPreview = LocalInspectionMode.current
    // TODO(b/336693596): UIModeManager is not yet supported in preview
    if (!isPreview && isContrastAvailable()) {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
        val contrastLevel = uiModeManager?.contrast ?: 0.01f

        colorScheme = when (contrastLevel) {
            in 0.0f..0.33f -> if (isDark)
                darkScheme else lightScheme

            in 0.34f..0.66f -> if (isDark)
                mediumContrastDarkColorScheme else mediumContrastLightColorScheme

            in 0.67f..1.0f -> if (isDark)
                highContrastDarkColorScheme else highContrastLightColorScheme

            else -> if (isDark) darkScheme else lightScheme
        }
        return colorScheme
    } else return colorScheme
}

private class MobileThemeProvider : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        PocketNotesTheme {
            Surface {
                content()
            }
        }
    }

}

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
@PreviewWrapper(MobileThemeProvider::class)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class ThemePreviews
