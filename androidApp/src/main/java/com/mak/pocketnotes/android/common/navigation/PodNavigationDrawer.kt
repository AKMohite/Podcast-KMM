package com.mak.pocketnotes.android.common.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
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
    currentKey: NavKey?,
    modifier: Modifier = Modifier,
    bottomContent: @Composable ColumnScope.() -> Unit
) {
    PermanentDrawerSheet(
        modifier = modifier
            .sizeIn(minWidth = 200.dp, maxWidth = 250.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        bottomBarItems.forEach { item ->
            NavigationDrawerItem(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                selected = currentKey == item,
                onClick = {
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
        currentKey = Home,
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
