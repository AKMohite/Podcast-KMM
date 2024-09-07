package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.store.PodcastStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse

class GetPodcast: KoinComponent {

    private val store: PodcastStore by inject()
    private val dispatcher: Dispatcher by inject()

    operator fun invoke(id: String): Flow<Podcast> {
        return store().stream(StoreReadRequest.cached(id, refresh = false))
            .filter { response ->
                response is StoreReadResponse.Data
            }.map { response ->
                response.requireData()
            }.flowOn(dispatcher.io)
    }
}