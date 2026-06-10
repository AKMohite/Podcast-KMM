package app.mak.pocketnotes.wearos.presentation.theme

import androidx.wear.compose.material3.Typography
import app.mak.pocketnotes.core.designsystem.theme.typographyFromDefaults

val type = typographyFromDefaults()
val wearTypography = Typography(
    displayLarge = type.displayLarge,
    displayMedium = type.displayMedium,
    displaySmall = type.displaySmall,
    titleLarge = type.titleLarge,
    titleMedium = type.titleMedium,
    titleSmall = type.titleSmall,
    labelLarge = type.labelLarge,
    labelMedium = type.labelMedium,
    labelSmall = type.labelSmall,
    bodyLarge = type.bodyLarge,
    bodyMedium = type.bodyMedium,
    bodySmall = type.bodySmall,
)