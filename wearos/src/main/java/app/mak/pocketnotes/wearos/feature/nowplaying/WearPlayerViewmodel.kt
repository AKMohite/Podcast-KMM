package app.mak.pocketnotes.wearos.feature.nowplaying

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class WearPlayerViewmodel: ViewModel() {
    val uiState: StateFlow<WearUiState> = MutableStateFlow(WearUiState())

    fun onEvent(event: WearPlayEvent) {}
}

data class WearUiState(
    val player: WearPlayerState = WearPlayerState.IDLE,
    val queue: WearQueueData    = WearQueueData(),
    val isConnected: Boolean    = false,
)

sealed interface WearPlayEvent {
    data object OnTogglePlay: WearPlayEvent
    data object OnSkipNext: WearPlayEvent
    data object OnSkipPrevious: WearPlayEvent
    data object OnSkipForward: WearPlayEvent
    data object OnSkipBackward: WearPlayEvent
    data object OnToggleShuffle: WearPlayEvent
    data object OnCycleSpeed: WearPlayEvent
}


//private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

@Serializable
data class WearPlayerState(
    val episodeId: String        = "",
    val episodeTitle: String     = "",
    val podcastTitle: String     = "",
    val artworkUrl: String       = "",
    val isPlaying: Boolean       = false,
    val isLoading: Boolean       = false,
    val positionMs: Long         = 0L,
    val durationMs: Long         = 0L,
    val playbackSpeed: Float     = 1.0f,
    val hasNext: Boolean         = false,
    val hasPrevious: Boolean     = false,
    val queueSize: Int           = 0,
    val currentQueueIndex: Int   = -1,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: WearRepeatMode = WearRepeatMode.NONE,
) {
    val progress: Float
        get() = if (durationMs > 0L) positionMs.toFloat() / durationMs else 0f

    val remainingMs: Long
        get() = (durationMs - positionMs).coerceAtLeast(0L)

    val isIdle: Boolean get() = episodeId.isEmpty()

//    fun toJson(): String = json.encodeToString(serializer(), this)

    companion object {
        val IDLE = WearPlayerState()
//        fun fromJson(raw: String): WearPlayerState = runCatching {
//            json.decodeFromString(serializer(), raw)
//        }.getOrDefault(IDLE)
    }
}

@Serializable
enum class WearRepeatMode { NONE, ONE, ALL }

// ─────────────────────────────────────────────────────────────────────────────
// WearQueueItem — stripped-down episode for the watch queue list
// ─────────────────────────────────────────────────────────────────────────────

@Serializable
data class WearQueueItem(
    val index: Int,
    val episodeId: String,
    val title: String,
    val podcastTitle: String,
    val durationMs: Long,
    val artworkUrl: String,
)

@Serializable
data class WearQueueData(val items: List<WearQueueItem> = emptyList()) {
//    fun toJson(): String = json.encodeToString(serializer(), this)
    companion object {
//        fun fromJson(raw: String): WearQueueData = runCatching {
//            json.decodeFromString(serializer(), raw)
//        }.getOrDefault(WearQueueData())
    }
}
