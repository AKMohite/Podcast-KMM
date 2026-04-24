package com.mak.pocketnotes.domain.models

data class AppSettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: String = "en",
    val textSize: TextSize = TextSize.MEDIUM,
    val autoPlayNext: Boolean = true,
    val downloadOnWifiOnly: Boolean = true,
    val episodeDeleteAfterPlayed: Boolean = false,
    val streamingQuality: StreamQuality = StreamQuality.MEDIUM,
)

enum class AppTheme   { LIGHT, DARK, SYSTEM }
enum class StreamQuality { LOW, MEDIUM, HIGH }

enum class TextSize {
    SMALL, MEDIUM, LARGE, EXTRA_LARGE
}
