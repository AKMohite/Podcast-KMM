package com.mak.pocketnotes.android.feature.settings

import app.cash.turbine.test
import com.mak.pocketnotes.data.repository.SettingsRepository
import com.mak.pocketnotes.domain.models.AppSettings
import com.mak.pocketnotes.domain.models.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {
  private class FakeSettingsRepository : SettingsRepository {
    val settingsFlow = MutableStateFlow(AppSettings())
    var lastUpdatedSettings: AppSettings? = null

    override fun getSettings(): Flow<AppSettings> = settingsFlow

    override suspend fun updateSettings(settings: AppSettings) {
      lastUpdatedSettings = settings
      settingsFlow.value = settings
    }
  }

  private val repository = FakeSettingsRepository()
  private val testDispatcher = UnconfinedTestDispatcher()
  private lateinit var viewModel: SettingsViewModel

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    viewModel = SettingsViewModel(repository)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `initial state loads settings`() = runTest {
    viewModel.state.test {
      val state = awaitItem()
      assertEquals(AppSettings(), state.settings)
      assertEquals(false, state.isLoading)
    }
  }

  @Test
  fun `onAction OnThemeChange updates repository and state`() = runTest {
    viewModel.state.test {
      awaitItem()

      viewModel.onAction(SettingsAction.OnThemeChange(AppTheme.DARK))

      val updatedState = awaitItem()
      assertEquals(AppTheme.DARK, updatedState.settings.theme)
      assertEquals(AppTheme.DARK, repository.lastUpdatedSettings?.theme)
    }
  }
}
