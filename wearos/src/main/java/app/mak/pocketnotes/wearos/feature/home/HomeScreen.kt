package app.mak.pocketnotes.wearos.feature.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import app.mak.pocketnotes.wearos.R
import app.mak.pocketnotes.wearos.presentation.theme.WearPocketNotesTheme

sealed interface HomeNavigation {
    data object Podcasts : HomeNavigation
    data object Downloads : HomeNavigation
    data object Subscribed : HomeNavigation
    data object Settings : HomeNavigation
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateTo: (HomeNavigation) -> Unit
) {
    ScalingLazyColumn(
        state = rememberScalingLazyListState(),
        modifier = modifier
            .fillMaxWidth()
    ) {

        item {
            HomeChip(
                title = stringResource(R.string.home_podcasts),
                icon = Icons.Default.Podcasts,
                onClick = { navigateTo(HomeNavigation.Podcasts) }
            )
        }

        item {
            HomeChip(
                title = stringResource(R.string.home_downloads),
                icon = Icons.Default.Download,
                onClick = { navigateTo(HomeNavigation.Downloads) }
            )
        }

        item {
            HomeChip(
                title = stringResource(R.string.home_subscribed),
                icon = Icons.Default.Subscriptions,
                onClick = { navigateTo(HomeNavigation.Subscribed) }
            )
        }

        item {
            HomeChip(
                title = stringResource(R.string.home_settings),
                icon = Icons.Default.Settings,
                onClick = { navigateTo(HomeNavigation.Settings) }
            )
        }
    }
}

@Composable
fun HomeChip(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Chip(
        onClick = onClick,
        label = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.onPrimary
            )
        },
        modifier = modifier
            .fillMaxWidth(),
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(ChipDefaults.IconSize),
            )
        }
    )
}

@Preview(device = WearDevices.SMALL_ROUND)
@Composable
private fun WatchListPreview() {
    WearPocketNotesTheme {
        HomeScreen(
            modifier = Modifier,
            navigateTo = {}
        )
    }
}