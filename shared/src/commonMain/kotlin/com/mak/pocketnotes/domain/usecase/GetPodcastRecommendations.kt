package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.store.RelatedPodcastsStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse

class GetPodcastRecommendations: KoinComponent {

    private val store: RelatedPodcastsStore by inject()
    private val dispatcher: Dispatcher by inject()

    operator fun invoke(id: String): Flow<List<Podcast>> {
        return store.invoke(id).stream(StoreReadRequest.cached(key = id, refresh = false))
            .filter { storeResponse ->
                storeResponse is StoreReadResponse.Data
            }.map { storeResponse ->
                storeResponse.requireData()
            }.flowOn(dispatcher.io)
    }
}