package com.mak.pocketnotes.android.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import app.mak.pocketnotes.core.designsystem.theme.backgroundDark
import app.mak.pocketnotes.core.designsystem.theme.backgroundDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.backgroundDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.backgroundLight
import app.mak.pocketnotes.core.designsystem.theme.backgroundLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.backgroundLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.errorContainerDark
import app.mak.pocketnotes.core.designsystem.theme.errorContainerDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.errorContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.errorContainerLight
import app.mak.pocketnotes.core.designsystem.theme.errorContainerLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.errorContainerLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.errorDark
import app.mak.pocketnotes.core.designsystem.theme.errorDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.errorDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.errorLight
import app.mak.pocketnotes.core.designsystem.theme.errorLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.errorLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.inverseOnSurfaceDark
import app.mak.pocketnotes.core.designsystem.theme.inverseOnSurfaceDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.inverseOnSurfaceDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.inverseOnSurfaceLight
import app.mak.pocketnotes.core.designsystem.theme.inverseOnSurfaceLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.inverseOnSurfaceLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.inversePrimaryDark
import app.mak.pocketnotes.core.designsystem.theme.inversePrimaryDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.inversePrimaryDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.inversePrimaryLight
import app.mak.pocketnotes.core.designsystem.theme.inversePrimaryLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.inversePrimaryLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.inverseSurfaceDark
import app.mak.pocketnotes.core.designsystem.theme.inverseSurfaceDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.inverseSurfaceDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.inverseSurfaceLight
import app.mak.pocketnotes.core.designsystem.theme.inverseSurfaceLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.inverseSurfaceLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onBackgroundDark
import app.mak.pocketnotes.core.designsystem.theme.onBackgroundDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onBackgroundDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onBackgroundLight
import app.mak.pocketnotes.core.designsystem.theme.onBackgroundLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onBackgroundLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onErrorContainerDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorContainerLight
import app.mak.pocketnotes.core.designsystem.theme.onErrorContainerLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorContainerLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorDark
import app.mak.pocketnotes.core.designsystem.theme.onErrorDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorLight
import app.mak.pocketnotes.core.designsystem.theme.onErrorLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onErrorLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryContainerDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryContainerLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryContainerLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryDark
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryLight
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryContainerDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryContainerLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryContainerLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryDark
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryLight
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceDark
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceLight
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantDark
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantLight
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryContainerDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryContainerLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryContainerLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryDark
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryLight
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.outlineDark
import app.mak.pocketnotes.core.designsystem.theme.outlineDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.outlineDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.outlineLight
import app.mak.pocketnotes.core.designsystem.theme.outlineLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.outlineLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.outlineVariantDark
import app.mak.pocketnotes.core.designsystem.theme.outlineVariantDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.outlineVariantDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.outlineVariantLight
import app.mak.pocketnotes.core.designsystem.theme.outlineVariantLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.outlineVariantLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.primaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.primaryContainerDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.primaryContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.primaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.primaryContainerLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.primaryContainerLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.primaryDark
import app.mak.pocketnotes.core.designsystem.theme.primaryDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.primaryDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.primaryLight
import app.mak.pocketnotes.core.designsystem.theme.primaryLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.primaryLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.scrimDark
import app.mak.pocketnotes.core.designsystem.theme.scrimDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.scrimDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.scrimLight
import app.mak.pocketnotes.core.designsystem.theme.scrimLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.scrimLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.secondaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.secondaryContainerDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.secondaryContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.secondaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.secondaryContainerLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.secondaryContainerLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.secondaryDark
import app.mak.pocketnotes.core.designsystem.theme.secondaryDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.secondaryDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.secondaryLight
import app.mak.pocketnotes.core.designsystem.theme.secondaryLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.secondaryLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceBrightDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceBrightDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceBrightDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceBrightLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceBrightLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceBrightLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighestDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighestDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighestDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighestLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighestLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerHighestLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowestDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowestDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowestDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowestLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowestLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceContainerLowestLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceDimDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceDimDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceDimDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceDimLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceDimLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceDimLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceVariantDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceVariantDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceVariantDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceVariantLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceVariantLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.surfaceVariantLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.tertiaryContainerDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryContainerDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.tertiaryContainerLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryContainerLightMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryDark
import app.mak.pocketnotes.core.designsystem.theme.tertiaryDarkHighContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryDarkMediumContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryLight
import app.mak.pocketnotes.core.designsystem.theme.tertiaryLightHighContrast
import app.mak.pocketnotes.core.designsystem.theme.tertiaryLightMediumContrast

internal val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

internal val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

internal val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

internal val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

internal val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

internal val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)
