package com.mak.pocketnotes.android.common.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mak.pocketnotes.android.common.BottomDestination

@Composable
internal fun PodPermanentDrawer(
    bottomBarItems: List<BottomDestination>,
    onBottomNavigate: (BottomDestination) -> Unit,
    currentScreen: NavDestination?,
    content: @Composable () -> Unit
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    PermanentNavigationDrawer(drawerContent = {
        PermanentDrawerSheet {
            bottomBarItems.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    selected = currentScreen?.hierarchy?.any { it.route == item.route } == true,
                    onClick = {
                        selectedItem = index
                        onBottomNavigate(item)
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(id = item.title)
                        )
                    },
                    label = { Text(text = stringResource(id = item.title)) }
                )
            }
        }
    }) {
        content()
    }
}