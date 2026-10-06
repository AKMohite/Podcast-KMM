package com.mak.pocketnotes.android.di

import androidx.lifecycle.SavedStateHandle
import com.mak.pocketnotes.core.common.WidgetUpdater
import com.mak.pocketnotes.core.common.utils.AppConfig
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.CuratedPodcastDAO
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.EpisodePagingKeysDAO
import com.mak.pocketnotes.core.database.dao.GenresDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.RelatedPodcastDAO
import com.mak.pocketnotes.core.database.dao.SubscriptionDAO
import com.mak.pocketnotes.core.database.dao.TrendingPodcastDAO
import com.mak.pocketnotes.core.feature.data.di.coreDataModule
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.RelatedPodcastRepository
import com.mak.pocketnotes.core.feature.domain.search.repository.GenreRepository
import com.mak.pocketnotes.core.feature.domain.search.repository.SearchRepository
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.data.repository.SettingsRepository
import com.mak.pocketnotes.domain.usecase.SearchPodcast
import kotlinx.coroutines.CoroutineDispatcher
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
        SavedStateHandle::class,
        AppConfig::class,
        WidgetUpdater::class,
        BestPodcastRepository::class,
        CuratedPodcastRepository::class,
        PodcastRepository::class,
        RelatedPodcastRepository::class,
        EpisodeRepository::class,
        GenreRepository::class,
        SearchRepository::class,
        SearchPodcast::class,
        SettingsRepository::class
      )
    )
  }

  @OptIn(KoinExperimentalAPI::class)
  @Test
  fun verifyCoreDataModuleDependencies() {
    coreDataModule.verify(
      extraTypes = listOf(
        AppConfig::class,
        WidgetUpdater::class,
        PocketNotesAPI::class,
        DatabaseTransactionRunner::class,
        PodcastDAO::class,
        TrendingPodcastDAO::class,
        CuratedPodcastDAO::class,
        EpisodeDAO::class,
        EpisodePagingKeysDAO::class,
        LastSyncDAO::class,
        SubscriptionDAO::class,
        RelatedPodcastDAO::class,
        GenresDAO::class,
        CoroutineDispatcher::class
      )
    )
  }
}
