package com.app.podcast.android.ui

import androidx.lifecycle.ViewModel
import com.app.podcast.domain.interactor.GetBestPodcastUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBestPodcast: GetBestPodcastUC
): ViewModel() {

    init {
        getBestPodcasts()
    }

    private fun getBestPodcasts(forceReload: Boolean = false) {
        getBestPodcast(forceReload).onEach { dataState ->
            dataState.isLoading
            dataState.data?.let { podcasts -> }
            dataState.message?.let { msg -> }
        }
    }
}