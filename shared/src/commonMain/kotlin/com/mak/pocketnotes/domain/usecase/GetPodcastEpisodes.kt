package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.domain.store.EpisodeStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse

class GetPodcastEpisodes: KoinComponent {
    private val store: EpisodeStore by inject()
    private val dispatcher: Dispatcher by inject()

    operator fun invoke(id: String, timeStamp: Long = Clock.System.now().toEpochMilliseconds()): Flow<List<PodcastEpisode>> {
        return store()
            .stream(StoreReadRequest.cached(key = Pair(id, timeStamp), refresh = false))
            .filter { response ->
                response is StoreReadResponse.Data
            }
            .map { response ->
                response.requireData()
            }
            .flowOn(dispatcher.io)
    }
}