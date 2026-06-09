package com.mak.pocketnotes.android.feature.player.v2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class PlayerExpansionViewModel: ViewModel() {

    private val _expansionState = MutableStateFlow(PlayerExpansionState())
    val expansionState: StateFlow<PlayerExpansionState> = _expansionState.asStateFlow()

    // Channel is one-shot: collected once in the root composable and dispatched
    // directly to the nav controller / scaffold state.
    private val _navEvents = Channel<NavEvent>(Channel.BUFFERED)
    val navEvents = _navEvents.receiveAsFlow()

    // ── Expansion control ─────────────────────────────────────────────────

    /**
     * Called when an episode starts playing so the mini player appears.
     * Should be called from the root composition whenever
     * `playerState.currentEpisode` transitions from null → non-null.
     */
    fun onEpisodeStarted() {
        _expansionState.update { it.copy(expansion = PlayerExpansion.MINI) }
    }

    fun onEpisodeStopped() {
        _expansionState.update { it.copy(expansion = PlayerExpansion.HIDDEN) }
    }

    /** Expand mini → full (Compact/Medium tap on mini bar). */
    fun expand() {
        _expansionState.update { it.copy(expansion = PlayerExpansion.FULL) }
    }

    /** Collapse full → mini (user drags down or taps "↓"). */
    fun collapse() {
        _expansionState.update { it.copy(expansion = PlayerExpansion.MINI) }
    }
}

/**
 * Controls whether the player panel / overlay is hidden, showing as a mini bar,
 * or fully expanded.  Used on ALL breakpoints:
 *
 *  Compact / Medium  →  hidden → mini bar → full-screen overlay (animated)
 *  Expanded+         →  hidden → always-visible side pane (no animation needed;
 *                                 the pane simply appears when an episode plays)
 */
enum class PlayerExpansion {
    /** No episode queued – player UI completely absent. */
    HIDDEN,

    /**
     * Compact/Medium: 72 dp bar at the bottom of the screen.
     * Expanded+: player pane is visible but this enum value isn't used (pane is
     * always "full" when visible).
     */
    MINI,

    /** Compact/Medium: slides up to fill the screen. Expanded+: pane is shown. */
    FULL,
}

data class PlayerExpansionState(
    val expansion: PlayerExpansion = PlayerExpansion.HIDDEN,
) {
    val isVisible: Boolean get() = expansion != PlayerExpansion.HIDDEN
    val isFullyExpanded: Boolean get() = expansion == PlayerExpansion.FULL
    val isMini: Boolean get() = expansion == PlayerExpansion.MINI
}

sealed interface NavEvent {
    /** Expand the player pane/overlay to full. */
    data object ShowPlayer : NavEvent

    /** Navigate to a specific episode detail AND start playback. */
    data class ShowEpisode(val episodeId: String) : NavEvent

    /** Navigate to the queue screen (Compact) or reveal queue pane (Expanded+). */
    data object ShowQueue : NavEvent
}

