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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
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
    currentKey: NavKey?,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier
            .fillMaxHeight(),
    ) {
        bottomBarItems.forEach { item ->
            NavigationRailItem(
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


@Composable
internal fun PodModalWideNavigationRail(
    bottomBarItems: List<BottomDestination>,
    modifier: Modifier = Modifier,
    currentKey: NavKey? = null,
    onBottomNavigate: (BottomDestination) -> Unit,
) {
    val state = rememberWideNavigationRailState()
    val scope = rememberCoroutineScope()
    val headerDescription =
        if (state.targetValue == WideNavigationRailValue.Expanded) {
            "Collapse rail"
        } else {
            "Expand rail"
        }

    ModalWideNavigationRail(
        modifier = modifier,
        state = state,
        expandedHeaderTopPadding = 64.dp,
        header = {
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
        bottomBarItems.forEach { item ->
            WideNavigationRailItem(
                railExpanded = state.targetValue == WideNavigationRailValue.Expanded,
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = stringResource(item.title),
                    )
                },
                label = { Text(stringResource(item.title)) },
                selected = currentKey == item,
                onClick = {
                    onBottomNavigate(item)
                },
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
        currentKey = Home
    )
}
