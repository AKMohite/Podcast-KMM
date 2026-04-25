package com.mak.pocketnotes.android.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.data.repository.SettingsRepository
import com.mak.pocketnotes.domain.models.AppSettings
import com.mak.pocketnotes.domain.models.AppTheme
import com.mak.pocketnotes.domain.models.StreamQuality
import com.mak.pocketnotes.domain.models.TextSize
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val repository: SettingsRepository
): ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    init {
        repository.getSettings()
            .distinctUntilChanged()
            .onEach { s -> _state.update { it.copy(settings = s, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnThemeChange              -> update { it.copy(theme = action.theme) }
            is SettingsAction.OnLanguageChange           -> update { it.copy(language = action.lang) }
            is SettingsAction.OnTextSizeChange           -> update { it.copy(textSize = action.size) }
            is SettingsAction.OnAutoPlayNextChange       -> update { it.copy(autoPlayNext = action.enabled) }
            is SettingsAction.OnDownloadWifiOnlyChange   -> update { it.copy(downloadOnWifiOnly = action.enabled) }
            is SettingsAction.OnDeleteAfterPlayedChange  -> update { it.copy(episodeDeleteAfterPlayed = action.enabled) }
            is SettingsAction.OnStreamQualityChange      -> update { it.copy(streamingQuality = action.quality) }
            SettingsAction.OnOpenPrivacyPolicy           -> viewModelScope.launch { _events.send(SettingsEvent.OpenUrl("https://www.google.com/privacy")) }
            SettingsAction.OnOpenSourceCode              -> viewModelScope.launch { _events.send(SettingsEvent.OpenUrl("https://github.com/app")) }
            SettingsAction.OnOpenLicenses                -> viewModelScope.launch { _events.send(SettingsEvent.NavigateToLicenses) }
        }
    }

    private fun update(transform: (AppSettings) -> AppSettings) {
        viewModelScope.launch {
            val newSettings = transform(state.value.settings)
            repository.updateSettings(newSettings)
            _state.update { it.copy(settings = newSettings) }
        }
    }
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