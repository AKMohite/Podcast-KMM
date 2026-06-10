package app.mak.pocketnotes.wearos.presentation.theme

import androidx.wear.compose.material3.ColorScheme
import app.mak.pocketnotes.core.designsystem.theme.backgroundDark
import app.mak.pocketnotes.core.designsystem.theme.errorContainerDark
import app.mak.pocketnotes.core.designsystem.theme.errorDark
import app.mak.pocketnotes.core.designsystem.theme.onBackgroundDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorDark
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryDark
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantDark
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryDark
import app.mak.pocketnotes.core.designsystem.theme.outlineDark
import app.mak.pocketnotes.core.designsystem.theme.outlineVariantDark
import app.mak.pocketnotes.core.designsystem.theme.primaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.primaryDark
import app.mak.pocketnotes.core.designsystem.theme.secondaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.secondaryDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryDark

internal val wearColorScheme = ColorScheme(
    primary = primaryDark,
    primaryDim = primaryDark,
    onPrimary = onTertiaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    secondaryDim = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDark,
    onBackground = onBackgroundDarkMediumContrast,
    onSurface = onSurfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDark,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
)