package com.mak.pocketnotes.android.common.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mak.pocketnotes.android.common.BottomDestination
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed

@Composable
internal fun PodNavigationRail(
    bottomBarItems: List<BottomDestination>,
    onBottomNavigate: (BottomDestination) -> Unit,
    currentScreen: NavDestination?,
    modifier: Modifier = Modifier
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
    ) {
        bottomBarItems.forEachIndexed { index, item ->
            NavigationRailItem(
                selected = currentScreen?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    selectedItem = index
                    onBottomNavigate(item)
                },
                icon = { Icon(imageVector = item.icon, contentDescription = stringResource(id = item.title)) },
                label = { Text(text = stringResource(id = item.title)) }
            )
        }
    }
}

@Preview
@Composable
private fun PodNavigationRailPreview() {
    PodNavigationRail(
        bottomBarItems = listOf(
            Home,
            Search,
            Subscribed,
            Settings
        ),
        onBottomNavigate = {},
        currentScreen = null
    )
}