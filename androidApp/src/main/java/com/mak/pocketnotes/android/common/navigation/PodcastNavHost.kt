package com.mak.pocketnotes.android.common.navigation

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.viewmodel.MediaViewModel
import com.mak.pocketnotes.android.feature.home.discoverEntry
import com.mak.pocketnotes.android.feature.player.nowPlayingEntry
import com.mak.pocketnotes.android.feature.podcastdetail.podcastDetailEntry
import com.mak.pocketnotes.android.feature.search.searchEntry
import com.mak.pocketnotes.android.feature.settings.settingsEntry
import com.mak.pocketnotes.domain.models.asPlayableEpisodes

@Composable
internal fun PodcastNavDisplay(
    navigationState: NavigationState,
    navigator: Navigator,
    startService: () -> Unit,
    mediaViewModel: MediaViewModel,
    modifier: Modifier = Modifier,
) {
    val entryProvider : (NavKey) -> NavEntry<NavKey> = entryProvider {
        discoverEntry(navigator)

        podcastDetailEntry(navigator) { episodes ->
            startService()
            mediaViewModel.loadMedia(episodes.asPlayableEpisodes())
        }

        nowPlayingEntry(mediaViewModel, navigator)

        searchEntry(navigator)

        entry<Subscribed> {
            EmptyScreen(Subscribed.title)
        }

        settingsEntry()
    }

    NavDisplay(
        modifier = modifier,
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() }
    )
}


@Composable
internal fun EmptyScreen(
    @StringRes title: Int
) {
    Text(text = "${stringResource(id = title)} Work in progress", style = MaterialTheme.typography.headlineSmall)
}
