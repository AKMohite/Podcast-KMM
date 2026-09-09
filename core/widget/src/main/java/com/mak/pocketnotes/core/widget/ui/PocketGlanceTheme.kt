package com.mak.pocketnotes.core.widget.ui

import androidx.compose.runtime.Composable
import androidx.glance.GlanceTheme
import androidx.glance.color.ColorProvider
import androidx.glance.color.colorProviders
import app.mak.pocketnotes.core.designsystem.theme.backgroundDark
import app.mak.pocketnotes.core.designsystem.theme.backgroundLight
import app.mak.pocketnotes.core.designsystem.theme.errorContainerDark
import app.mak.pocketnotes.core.designsystem.theme.errorContainerLight
import app.mak.pocketnotes.core.designsystem.theme.errorDark
import app.mak.pocketnotes.core.designsystem.theme.errorLight
import app.mak.pocketnotes.core.designsystem.theme.inverseOnSurfaceDark
import app.mak.pocketnotes.core.designsystem.theme.inverseOnSurfaceLight
import app.mak.pocketnotes.core.designsystem.theme.inversePrimaryDark
import app.mak.pocketnotes.core.designsystem.theme.inversePrimaryLight
import app.mak.pocketnotes.core.designsystem.theme.inverseSurfaceDark
import app.mak.pocketnotes.core.designsystem.theme.inverseSurfaceLight
import app.mak.pocketnotes.core.designsystem.theme.onBackgroundDark
import app.mak.pocketnotes.core.designsystem.theme.onBackgroundLight
import app.mak.pocketnotes.core.designsystem.theme.onErrorContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onErrorContainerLight
import app.mak.pocketnotes.core.designsystem.theme.onErrorDark
import app.mak.pocketnotes.core.designsystem.theme.onErrorLight
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryDark
import app.mak.pocketnotes.core.designsystem.theme.onPrimaryLight
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryDark
import app.mak.pocketnotes.core.designsystem.theme.onSecondaryLight
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceDark
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceLight
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantDark
import app.mak.pocketnotes.core.designsystem.theme.onSurfaceVariantLight
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryDark
import app.mak.pocketnotes.core.designsystem.theme.onTertiaryLight
import app.mak.pocketnotes.core.designsystem.theme.outlineDark
import app.mak.pocketnotes.core.designsystem.theme.outlineLight
import app.mak.pocketnotes.core.designsystem.theme.primaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.primaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.primaryDark
import app.mak.pocketnotes.core.designsystem.theme.primaryLight
import app.mak.pocketnotes.core.designsystem.theme.secondaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.secondaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.secondaryDark
import app.mak.pocketnotes.core.designsystem.theme.secondaryLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceLight
import app.mak.pocketnotes.core.designsystem.theme.surfaceVariantDark
import app.mak.pocketnotes.core.designsystem.theme.surfaceVariantLight
import app.mak.pocketnotes.core.designsystem.theme.tertiaryContainerDark
import app.mak.pocketnotes.core.designsystem.theme.tertiaryContainerLight
import app.mak.pocketnotes.core.designsystem.theme.tertiaryDark
import app.mak.pocketnotes.core.designsystem.theme.tertiaryLight

private val colors = colorProviders(
  primary = ColorProvider(day = primaryLight, night = primaryDark),
  onPrimary = ColorProvider(day = onPrimaryLight, night = onPrimaryDark),
  primaryContainer = ColorProvider(day = primaryContainerLight, night = primaryContainerDark),
  onPrimaryContainer = ColorProvider(day = onPrimaryContainerLight, night = onPrimaryContainerDark),
  secondary = ColorProvider(day = secondaryLight, night = secondaryDark),
  onSecondary = ColorProvider(day = onSecondaryLight, night = onSecondaryDark),
  secondaryContainer = ColorProvider(day = secondaryContainerLight, night = secondaryContainerDark),
  onSecondaryContainer = ColorProvider(
    day = onSecondaryContainerLight,
    night = onSecondaryContainerDark
  ),
  tertiary = ColorProvider(day = tertiaryLight, night = tertiaryDark),
  onTertiary = ColorProvider(day = onTertiaryLight, night = onTertiaryDark),
  tertiaryContainer = ColorProvider(day = tertiaryContainerLight, night = tertiaryContainerDark),
  onTertiaryContainer = ColorProvider(
    day = onTertiaryContainerLight,
    night = onTertiaryContainerDark
  ),
  error = ColorProvider(day = errorLight, night = errorDark),
  errorContainer = ColorProvider(day = errorContainerLight, night = errorContainerDark),
  onError = ColorProvider(day = onErrorLight, night = onErrorDark),
  onErrorContainer = ColorProvider(day = onErrorContainerLight, night = onErrorContainerDark),
  background = ColorProvider(day = backgroundLight, night = backgroundDark),
  onBackground = ColorProvider(day = onBackgroundLight, night = onBackgroundDark),
  surface = ColorProvider(day = surfaceLight, night = surfaceDark),
  onSurface = ColorProvider(day = onSurfaceLight, night = onSurfaceDark),
  surfaceVariant = ColorProvider(day = surfaceVariantLight, night = surfaceVariantDark),
  onSurfaceVariant = ColorProvider(day = onSurfaceVariantLight, night = onSurfaceVariantDark),
  outline = ColorProvider(day = outlineLight, night = outlineDark),
  inverseOnSurface = ColorProvider(day = inverseOnSurfaceLight, night = inverseOnSurfaceDark),
  inverseSurface = ColorProvider(day = inverseSurfaceLight, night = inverseSurfaceDark),
  inversePrimary = ColorProvider(day = inversePrimaryLight, night = inversePrimaryDark),
  widgetBackground = ColorProvider(day = backgroundLight, night = backgroundDark),
)

@Composable
internal fun PocketGlanceTheme(
  content: @Composable () -> Unit
) {
  GlanceTheme(
    colors = colors
  ) {
    content()
  }
}
