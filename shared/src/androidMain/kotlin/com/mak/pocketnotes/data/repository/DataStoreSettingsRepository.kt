package com.mak.pocketnotes.data.repository

import androidx.datastore.core.DataStore
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.models.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal class DataStoreSettingsRepository(
    private val dataStore: DataStore<AppSettings>,
    private val dispatcher: Dispatcher
): SettingsRepository {

//    private companion object Keys {
//        val THEME           = stringPreferencesKey("theme")
//        val LANGUAGE        = stringPreferencesKey("language")
//        val TEXT_SIZE       = stringPreferencesKey("text_size")
//        val AUTO_PLAY       = booleanPreferencesKey("auto_play")
//        val WIFI_ONLY       = booleanPreferencesKey("wifi_only")
//        val DELETE_PLAYED   = booleanPreferencesKey("delete_played")
//        val STREAM_QUALITY  = stringPreferencesKey("stream_quality")
//    }

    override fun getSettings(): Flow<AppSettings> {
        return dataStore.data
            .catch { emit(AppSettings()) }
            .flowOn(dispatcher.io)
//            .map { prefs ->
//                AppSettings(
//                    theme                   = prefs[THEME]?.let { AppTheme.valueOf(it) } ?: AppTheme.SYSTEM,
//                    language                = prefs[LANGUAGE] ?: "en",
//                    textSize                = prefs[TEXT_SIZE]?.let { TextSize.valueOf(it) } ?: TextSize.MEDIUM,
//                    autoPlayNext            = prefs[AUTO_PLAY] ?: true,
//                    downloadOnWifiOnly      = prefs[WIFI_ONLY] ?: true,
//                    episodeDeleteAfterPlayed = prefs[DELETE_PLAYED] ?: false,
//                    streamingQuality        = prefs[STREAM_QUALITY]?.let { StreamQuality.valueOf(it) } ?: StreamQuality.MEDIUM,
//                )
//            }
    }

    override suspend fun updateSettings(settings: AppSettings): Unit = withContext(dispatcher.io) {
//        dataStore.edit { prefs ->
//            prefs[THEME]          = settings.theme.name
//            prefs[LANGUAGE]       = settings.language
//            prefs[TEXT_SIZE]      = settings.textSize.name
//            prefs[AUTO_PLAY]      = settings.autoPlayNext
//            prefs[WIFI_ONLY]      = settings.downloadOnWifiOnly
//            prefs[DELETE_PLAYED]  = settings.episodeDeleteAfterPlayed
//            prefs[STREAM_QUALITY] = settings.streamingQuality.name
//        }
        dataStore.updateData { _ ->
            settings
        }
    }
}