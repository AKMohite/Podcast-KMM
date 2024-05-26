package com.mak.pocketnotes.android.feature.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R

internal data class SettingsOption(
    val title: String,
    val iconResId: ImageVector,
    val onClick: () -> Unit
)

private val settingsOption = listOf(
    SettingsOption(
        title = "Rate App",
        iconResId = Icons.Outlined.Star,
        onClick = {}
    ),
    SettingsOption(
        title = "Contact us",
        iconResId = Icons.Outlined.Email,
        onClick = {}
    ),
    SettingsOption(
        title = "Terms of Service",
        iconResId = Icons.AutoMirrored.Outlined.List,
        onClick = {}
    ),
    SettingsOption(
        title = "Privacy Policy",
        iconResId = Icons.AutoMirrored.Outlined.List,
        onClick = {}
    ),
    SettingsOption(
        title = "API reference",
        iconResId = Icons.Outlined.Build,
        onClick = {}
    ),
    SettingsOption(
        title = "View app code",
        iconResId = Icons.Outlined.Info,
        onClick = {}
    )
)

@Composable
internal fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                text = stringResource(id = R.string.bottom_settings),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        items(
            items = settingsOption
        ) { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { option.onClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = option.iconResId,
                    contentDescription = option.title,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .height(30.dp),
                    painter = painterResource(id = R.drawable.img_listen_notes),
                    contentDescription = stringResource(R.string.powered_by),
                    contentScale = ContentScale.Inside,
                    alignment = Alignment.Center
                )
            }
        }
    }
}