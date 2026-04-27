package com.mak.pocketnotes.android.common.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.common.BottomDestination
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed

@Composable
internal fun PodBottomNavigation(
    bottomBarItems: List<BottomDestination>,
    onBottomNavigate: (BottomDestination) -> Unit,
    currentKey: NavKey?
) {
    NavigationBar {
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

@Preview
@Composable
private fun PodBottomNavigationPreview() {
    PodBottomNavigation(
        bottomBarItems = listOf(
            Home,
            Search,
            Subscribed,
            Settings
        ),
        onBottomNavigate = {},
        currentKey = Home
    )
}
