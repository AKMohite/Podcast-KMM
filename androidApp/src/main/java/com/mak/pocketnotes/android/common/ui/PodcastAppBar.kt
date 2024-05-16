package com.mak.pocketnotes.android.common.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.ScreenDestination
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme

@Composable
internal fun PodcastAppBar(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    currentScreen: ScreenDestination,
    onNavigateBack: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
            }
            
            Text(
                modifier = Modifier.padding(12.dp),
                text = stringResource(currentScreen.title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
private fun PodcastAppBarPreview() {
    PocketNotesTheme {
        PodcastAppBar(
            currentScreen = PodcastDetail,
            onNavigateBack = {}
        )
    }
}