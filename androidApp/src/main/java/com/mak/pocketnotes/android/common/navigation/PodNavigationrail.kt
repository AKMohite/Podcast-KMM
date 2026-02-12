package com.mak.pocketnotes.android.common.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mak.pocketnotes.android.common.BottomDestination
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import kotlinx.coroutines.launch

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
            .fillMaxHeight(),
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


@Composable
internal fun PodModalWideNavigationRail(
    bottomBarItems: List<BottomDestination>,
    modifier: Modifier = Modifier,
    currentScreen: NavDestination? = null,
    onBottomNavigate: (BottomDestination) -> Unit,
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
//    val items = listOf("Home", "Search", "Settings")
//    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Star)
//    val unselectedIcons =
//        listOf(Icons.Outlined.Home, Icons.Outlined.FavoriteBorder, Icons.Outlined.StarBorder)
    val state = rememberWideNavigationRailState()
    val scope = rememberCoroutineScope()
    val headerDescription =
        if (state.targetValue == WideNavigationRailValue.Expanded) {
            "Collapse rail"
        } else {
            "Expand rail"
        }

//    Row(Modifier.fillMaxWidth()) {
        ModalWideNavigationRail(
            modifier = modifier,
            state = state,
            // Note: the value of expandedHeaderTopPadding depends on the layout of your screen in
            // order to achieve the best alignment.
            expandedHeaderTopPadding = 64.dp,
            header = {
                // Header icon button should have a tooltip.
                TooltipBox(
                    positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Above
                        ),
                    tooltip = { PlainTooltip { Text(headerDescription) } },
                    state = rememberTooltipState(),
                ) {
                    IconButton(
                        modifier =
                            Modifier.padding(start = 24.dp).semantics {
                                // The button must announce the expanded or collapsed state of the
                                // rail for accessibility.
                                stateDescription =
                                    if (state.currentValue == WideNavigationRailValue.Expanded) {
                                        "Expanded"
                                    } else {
                                        "Collapsed"
                                    }
                            },
                        onClick = {
                            scope.launch {
                                if (state.targetValue == WideNavigationRailValue.Expanded)
                                    state.collapse()
                                else state.expand()
                            }
                        },
                    ) {
                        if (state.targetValue == WideNavigationRailValue.Expanded) {
                            Icon(Icons.AutoMirrored.Filled.MenuOpen, headerDescription)
                        } else {
                            Icon(Icons.Filled.Menu, headerDescription)
                        }
                    }
                }
            },
        ) {
            bottomBarItems.forEachIndexed { index, item ->
                WideNavigationRailItem(
                    railExpanded = state.targetValue == WideNavigationRailValue.Expanded,
                    icon = {
                        Icon(
                            item.icon,
//                            if (selectedItem == index) selectedIcons[index]
//                            else unselectedIcons[index],
                            contentDescription = stringResource(item.title),
                        )
                    },
                    label = { Text(stringResource(item.title)) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        onBottomNavigate(bottomBarItems[index])
                    },
                )
            }
        }

//        val textString =
//            if (state.currentValue == WideNavigationRailValue.Expanded) {
//                "Expanded"
//            } else {
//                "Collapsed"
//            }
//        Column {
//            Text(modifier = Modifier.padding(16.dp), text = "The rail is $textString.")
//            Text(
//                modifier = Modifier.padding(16.dp),
//                text =
//                    "Note: This demo is best shown in portrait mode, as landscape mode" +
//                            " may result in a compact height in certain devices. For any" +
//                            " compact screen dimensions, use a Navigation Bar instead.",
//            )
//        }
//    }
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