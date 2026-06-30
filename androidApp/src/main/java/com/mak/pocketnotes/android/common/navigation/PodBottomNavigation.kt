package com.mak.pocketnotes.android.common.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.common.BottomDestination
import com.mak.pocketnotes.android.common.Discover
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.ui.theme.ThemePreviews

@Composable
internal fun PodBottomNavigation(
    bottomBarItems: List<BottomDestination>,
    onBottomNavigate: (BottomDestination) -> Unit,
    currentKey: NavKey?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        bottomBarItems.forEach { item ->
            NavigationBarItem(
                selected = currentKey == item,
                onClick = {
                    onBottomNavigate(item)
                },
                icon = { Icon(imageVector = item.icon, contentDescription = stringResource(id = item.title)) },
                label = { Text(text = stringResource(id = item.title)) }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun PodBottomNavigationPreview() {
    PodBottomNavigation(
        bottomBarItems = listOf(
            Discover,
            Search,
            Subscribed,
            Settings
        ),
        onBottomNavigate = {},
        currentKey = Discover,
    )
}
