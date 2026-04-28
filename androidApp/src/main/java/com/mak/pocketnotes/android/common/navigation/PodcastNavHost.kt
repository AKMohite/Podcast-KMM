package com.mak.pocketnotes.android.common.navigation

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.PodcastPlayer
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.viewmodel.MediaViewModel
import com.mak.pocketnotes.android.common.viewmodel.UIEvent
import com.mak.pocketnotes.android.feature.home.HomeScreen
import com.mak.pocketnotes.android.feature.home.HomeViewModel
import com.mak.pocketnotes.android.feature.home.discoverEntry
import com.mak.pocketnotes.android.feature.player.NowPlayingScreen
import com.mak.pocketnotes.android.feature.player.nowPlayingEntry
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailScreen
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailViewModel
import com.mak.pocketnotes.android.feature.podcastdetail.podcastDetailEntry
import com.mak.pocketnotes.android.feature.search.SearchScreen
import com.mak.pocketnotes.android.feature.search.SearchViewModel
import com.mak.pocketnotes.android.feature.search.searchEntry
import com.mak.pocketnotes.android.feature.settings.SettingsRoot
import com.mak.pocketnotes.android.feature.settings.settingsEntry
import com.mak.pocketnotes.domain.models.asPlayableEpisodes
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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

        podcastDetailEntry(startService, mediaViewModel, navigator)

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
