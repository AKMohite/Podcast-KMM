package com.mak.pocketnotes.android.feature.settings

import com.mak.pocketnotes.domain.models.AppSettings
import com.mak.pocketnotes.domain.models.AppTheme
import com.mak.pocketnotes.domain.models.StreamQuality
import com.mak.pocketnotes.domain.models.TextSize

internal class SettingsViewModel {
}

data class SettingsState(
    val settings: AppSettings   = AppSettings(),
    val isLoading: Boolean      = true,
    val appVersion: String      = "",
)

sealed interface SettingsAction {
    data class OnThemeChange(val theme: AppTheme) : SettingsAction
    data class OnLanguageChange(val lang: String) : SettingsAction
    data class OnTextSizeChange(val size: TextSize) : SettingsAction
    data class OnAutoPlayNextChange(val enabled: Boolean) : SettingsAction
    data class OnDownloadWifiOnlyChange(val enabled: Boolean) : SettingsAction
    data class OnDeleteAfterPlayedChange(val enabled: Boolean) : SettingsAction
    data class OnStreamQualityChange(val quality: StreamQuality) : SettingsAction
    data object OnOpenPrivacyPolicy : SettingsAction
    data object OnOpenLicenses : SettingsAction
    data object OnOpenSourceCode : SettingsAction
}

sealed interface SettingsEvent {
    data class OpenUrl(val url: String) : SettingsEvent
    data object NavigateToLicenses : SettingsEvent
}