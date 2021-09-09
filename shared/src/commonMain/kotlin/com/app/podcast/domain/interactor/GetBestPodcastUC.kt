package com.app.podcast.domain.interactor

import com.app.podcast.data.local.PodcastDB
import com.app.podcast.data.remote.PodcastAPI
import com.app.podcast.domain.DataState
import com.app.podcast.domain.model.BestPodcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetBestPodcastUC constructor(
    private val api: PodcastAPI,
    private val db: PodcastDB
) {

    @Throws(Exception::class)
    operator fun invoke(forceReload: Boolean): Flow<DataState<List<BestPodcast>>> = flow {
        try {
            emit(DataState.loading<List<BestPodcast>>())
            val cachedPodcasts = db.getBestPodcasts()
            if (cachedPodcasts.isNotEmpty() && !forceReload) {
                emit(DataState.data<List<BestPodcast>>(data = cachedPodcasts))
            } else {
                api.getBestPodcasts().also { dto ->
                    db.clearDatabase()
                    db.insertBestPodcast(dto.podcasts)
                }
                emit(DataState.data<List<BestPodcast>>(data = db.getBestPodcasts()))
            }
        } catch (e: Exception) {
            emit(DataState.error<List<BestPodcast>>(e.message ?: "Unknown error occurred"))
        }
    }

}