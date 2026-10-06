package com.mak.pocketnotes.android.di

import com.mak.pocketnotes.core.feature.data.di.coreDataModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class KoinModuleCheckTest : KoinTest {

  @OptIn(KoinExperimentalAPI::class)
  @Test
  fun verifyAppModuleDependencies() {
    appModule.verify(
      extraTypes = listOf(
        androidx.lifecycle.SavedStateHandle::class,
        com.mak.pocketnotes.core.common.utils.AppConfig::class,
        com.mak.pocketnotes.core.common.WidgetUpdater::class,
        com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository::class,
        com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository::class,
        com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository::class,
        com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.RelatedPodcastRepository::class,
        com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository::class,
        com.mak.pocketnotes.core.feature.domain.search.repository.GenreRepository::class,
        com.mak.pocketnotes.core.feature.domain.search.repository.SearchRepository::class,
        com.mak.pocketnotes.domain.usecase.SearchPodcast::class,
        com.mak.pocketnotes.data.repository.SettingsRepository::class
      )
    )
  }

  @OptIn(KoinExperimentalAPI::class)
  @Test
  fun verifyCoreDataModuleDependencies() {
    coreDataModule.verify(
      extraTypes = listOf(
        com.mak.pocketnotes.core.common.utils.AppConfig::class,
        com.mak.pocketnotes.core.common.WidgetUpdater::class,
        com.mak.pocketnotes.core.remote.PocketNotesAPI::class,
        com.mak.pocketnotes.core.database.DatabaseTransactionRunner::class,
        com.mak.pocketnotes.core.database.dao.PodcastDAO::class,
        com.mak.pocketnotes.core.database.dao.TrendingPodcastDAO::class,
        com.mak.pocketnotes.core.database.dao.CuratedPodcastDAO::class,
        com.mak.pocketnotes.core.database.dao.EpisodeDAO::class,
        com.mak.pocketnotes.core.database.dao.EpisodePagingKeysDAO::class,
        com.mak.pocketnotes.core.database.dao.LastSyncDAO::class,
        com.mak.pocketnotes.core.database.dao.SubscriptionDAO::class,
        com.mak.pocketnotes.core.database.dao.RelatedPodcastDAO::class,
        com.mak.pocketnotes.core.database.dao.GenresDAO::class,
        kotlinx.coroutines.CoroutineDispatcher::class
      )
    )
  }
}
