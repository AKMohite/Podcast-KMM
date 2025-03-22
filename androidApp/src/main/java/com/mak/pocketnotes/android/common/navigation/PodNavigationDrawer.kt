package com.mak.pocketnotes.android.common.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mak.pocketnotes.android.common.BottomDestination
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.ui.PermanentMinPlayer
import com.mak.pocketnotes.utils.sample.sampleEpisodes

@Composable
internal fun PodNavigationDrawer(
    bottomBarItems: List<BottomDestination>,
    onBottomNavigate: (BottomDestination) -> Unit,
    currentScreen: NavDestination?,
    modifier: Modifier = Modifier,
    bottomContent: @Composable ColumnScope.() -> Unit
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    PermanentDrawerSheet(
        modifier = modifier
            .sizeIn(minWidth = 200.dp, maxWidth = 300.dp)
    ) {
        bottomBarItems.forEachIndexed { index, item ->
            NavigationDrawerItem(
                selected = currentScreen?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    selectedItem = index
                    onBottomNavigate(item)
                },
                icon = { Icon(imageVector = item.icon, contentDescription = stringResource(id = item.title)) },
                label = { Text(text = stringResource(id = item.title)) }
            )
        }
        Spacer(Modifier.weight(1f))
        bottomContent()
        Spacer(Modifier.height(48.dp))
    }
}

@Preview
@Composable
private fun PodNavigationDrawerPreview() {
    PodNavigationDrawer(
        bottomBarItems = listOf(
            Home,
            Search,
            Subscribed,
            Settings
        ),
        onBottomNavigate = {},
        currentScreen = null,
        bottomContent = {
            PermanentMinPlayer(
                episode = sampleEpisodes[0].asPlayableEpisode(),
                playPause = {},
                isMediaPlaying = false,
                previousClick = {},
                nextClick = {}
            )
        }
    )
}