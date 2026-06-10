package app.mak.pocketnotes.wearos.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun WearPocketNotesTheme(
    content: @Composable () -> Unit
) {
    /**
     * Empty theme to customize for your app.
     * See: https://developer.android.com/jetpack/compose/designsystems/custom
     */
    MaterialTheme(
        colorScheme = wearColorScheme,
        typography = wearTypography,
        shapes = wearShapes,
        content = content
    )
}