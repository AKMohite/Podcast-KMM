package app.mak.pocketnotes.wearos.feature.home

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnItemScope
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeaderDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.TransformationSpec
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
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
    columnState: TransformingLazyColumnState = rememberTransformingLazyColumnState(),
    navigateTo: (HomeNavigation) -> Unit
) {
    val transformationSpec = rememberTransformationSpec()
    ScreenScaffold(
        scrollState = columnState,
    ) {
        TransformingLazyColumn(
            state = columnState,
            contentPadding = PaddingValues(
                top = ListHeaderDefaults.minimumTopListContentPadding,
                bottom = ListHeaderDefaults.minimumBottomListContentPadding,
                start = 8.dp,
                end = 8.dp
            ),
            modifier = modifier.fillMaxWidth()
        ) {
            item {
                HomeChip(
                    title = stringResource(R.string.home_podcasts),
                    icon = Icons.Default.Podcasts,
                    onClick = { navigateTo(HomeNavigation.Podcasts) },
                    transformationSpec = transformationSpec
                )
            }

            item {
                HomeChip(
                    title = stringResource(R.string.home_downloads),
                    icon = Icons.Default.Download,
                    onClick = { navigateTo(HomeNavigation.Downloads) },
                    transformationSpec = transformationSpec
                )
            }

            item {
                HomeChip(
                    title = stringResource(R.string.home_subscribed),
                    icon = Icons.Default.Subscriptions,
                    onClick = { navigateTo(HomeNavigation.Subscribed) },
                    transformationSpec = transformationSpec
                )
            }

            item {
                HomeChip(
                    title = stringResource(R.string.home_settings),
                    icon = Icons.Default.Settings,
                    onClick = { navigateTo(HomeNavigation.Settings) },
                    transformationSpec = transformationSpec
                )
            }
        }
    }
}

@Composable
fun TransformingLazyColumnItemScope.HomeChip(
    title: String,
    icon: ImageVector,
    transformationSpec: TransformationSpec,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        label = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .transformedHeight(this, transformationSpec),
        transformation = SurfaceTransformation(transformationSpec),
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
        }
    )
}

@WearPreviewDevices
@Composable
private fun WatchListPreview() {
    WearPocketNotesTheme {
        AppScaffold {
            val listState = rememberTransformingLazyColumnState()
            ScreenScaffold(
                scrollState = listState,
            ) {
                HomeScreen(
                    columnState = listState,
                    navigateTo = {}
                )
            }
        }
    }
}
