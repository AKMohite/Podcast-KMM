package com.mak.pocketnotes.data.repository

import com.mak.pocketnotes.domain.models.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>
    suspend fun updateSettings(settings: AppSettings)
}