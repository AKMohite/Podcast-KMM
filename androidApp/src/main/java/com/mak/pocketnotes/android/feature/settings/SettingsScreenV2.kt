package com.mak.pocketnotes.android.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.HighQuality
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.AppTheme
import com.mak.pocketnotes.domain.models.StreamQuality
import com.mak.pocketnotes.domain.models.TextSize
import org.koin.androidx.compose.koinViewModel


@Composable
internal fun SettingsRoot(viewModel: SettingsViewModel = koinViewModel()) {
    val state   = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    /*ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SettingsEvent.OpenUrl -> {
                runCatching {
                    CustomTabsIntent.Builder().setShowTitle(true).build()
                        .launchUrl(context, Uri.parse(event.url))
                }.onFailure {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.url)))
                }
            }
            SettingsEvent.NavigateToLicenses -> { *//* TODO: navigate to licenses screen *//* }
        }
    }*/

    SettingsScreenV2(state = state, onAction = viewModel::onAction)
}

@Composable
fun SettingsScreenV2(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // ── Page title ────────────────────────────────────────
        Text(
            text       = stringResource(R.string.settings_title),
            style      = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            modifier   = Modifier.padding(
                horizontal = 16.dp,
                vertical   = 16.dp,
            ),
        )

        // ── Appearance ────────────────────────────────────────
        SettingsSectionLabel(stringResource(R.string.settings_appearance))

        SettingsItem(
            icon     = Icons.Outlined.DarkMode,
            title    = stringResource(R.string.settings_theme),
            subtitle = state.settings.theme.displayName(),
        ) {
            SingleChoiceSegmentedButtonRow {
                AppTheme.entries.forEachIndexed { idx, theme ->
                    SegmentedButton(
                        selected = state.settings.theme == theme,
                        onClick  = { onAction(SettingsAction.OnThemeChange(theme)) },
                        shape    = SegmentedButtonDefaults.itemShape(idx, AppTheme.entries.size),
                    ) { Text(theme.displayName()) }
                }
            }
        }

        SettingsItem(
            icon     = Icons.Outlined.TextFields,
            title    = stringResource(R.string.settings_text_size),
            subtitle = state.settings.textSize.displayName(),
        ) {
            SingleChoiceSegmentedButtonRow {
                TextSize.entries.forEachIndexed { idx, size ->
                    SegmentedButton(
                        selected = state.settings.textSize == size,
                        onClick  = { onAction(SettingsAction.OnTextSizeChange(size)) },
                        shape    = SegmentedButtonDefaults.itemShape(idx, TextSize.entries.size),
                    ) { Text(size.shortLabel()) }
                }
            }
        }

        SettingsItem(
            icon     = Icons.Outlined.Language,
            title    = stringResource(R.string.settings_language),
            subtitle = state.settings.language.uppercase(),
        ) {
            // Language selection — simplified; in production use LocaleListCompat
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("en", "hi", "es", "fr", "de").forEach { lang ->
                    FilterChip(
                        selected = state.settings.language == lang,
                        onClick  = { onAction(SettingsAction.OnLanguageChange(lang)) },
                        label    = { Text(lang.uppercase()) },
                    )
                }
            }
        }

        // ── Playback ──────────────────────────────────────────
        SettingsSectionLabel(stringResource(R.string.settings_playback))

        SettingsToggle(
            icon     = Icons.Outlined.SkipNext,
            title    = stringResource(R.string.settings_autoplay),
            subtitle = stringResource(R.string.settings_autoplay_subtitle),
            checked  = state.settings.autoPlayNext,
            onToggle = { onAction(SettingsAction.OnAutoPlayNextChange(it)) },
        )

        SettingsItem(
            icon     = Icons.Outlined.HighQuality,
            title    = stringResource(R.string.settings_stream_quality),
            subtitle = state.settings.streamingQuality.displayName(),
        ) {
            SingleChoiceSegmentedButtonRow {
                StreamQuality.entries.forEachIndexed { idx, quality ->
                    SegmentedButton(
                        selected = state.settings.streamingQuality == quality,
                        onClick  = { onAction(SettingsAction.OnStreamQualityChange(quality)) },
                        shape    = SegmentedButtonDefaults.itemShape(idx, StreamQuality.entries.size),
                    ) { Text(quality.displayName()) }
                }
            }
        }

        // ── Storage ───────────────────────────────────────────
        SettingsSectionLabel(stringResource(R.string.settings_storage))

        SettingsToggle(
            icon     = Icons.Outlined.Wifi,
            title    = stringResource(R.string.settings_wifi_only),
            subtitle = stringResource(R.string.settings_wifi_only_subtitle),
            checked  = state.settings.downloadOnWifiOnly,
            onToggle = { onAction(SettingsAction.OnDownloadWifiOnlyChange(it)) },
        )

        SettingsToggle(
            icon     = Icons.Outlined.DeleteOutline,
            title    = stringResource(R.string.settings_delete_played),
            subtitle = stringResource(R.string.settings_delete_played_subtitle),
            checked  = state.settings.episodeDeleteAfterPlayed,
            onToggle = { onAction(SettingsAction.OnDeleteAfterPlayedChange(it)) },
        )

        // ── About ─────────────────────────────────────────────
        SettingsSectionLabel(stringResource(R.string.settings_about))

        SettingsClickRow(
            icon     = Icons.Outlined.Shield,
            title    = stringResource(R.string.settings_privacy_policy),
            onClick  = { onAction(SettingsAction.OnOpenPrivacyPolicy) },
        )

        SettingsClickRow(
            icon     = Icons.Outlined.Code,
            title    = stringResource(R.string.settings_source_code),
            onClick  = { onAction(SettingsAction.OnOpenSourceCode) },
        )

        SettingsClickRow(
            icon     = Icons.AutoMirrored.Outlined.LibraryBooks,
            title    = stringResource(R.string.settings_licenses),
            onClick  = { onAction(SettingsAction.OnOpenLicenses) },
        )

        SettingsClickRow(
            icon     = Icons.Outlined.Info,
            title    = stringResource(R.string.settings_app_version),
            subtitle = state.appVersion,
            onClick  = null,
        )

        Spacer(Modifier.height(88.dp))
    }
}

// ── Reusable settings sub-composables ─────────────────────────────
@Composable
private fun SettingsSectionLabel(title: String) {
    Text(
        text     = title,
        style    = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color    = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 4.dp),
    )
}

@Composable
private fun SettingsItem(icon: ImageVector, title: String, subtitle: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Column {
                Text(title, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        content()
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f))
}

@Composable
private fun SettingsToggle(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    ListItem(
        headlineContent  = { Text(title, fontWeight = FontWeight.Medium) },
        supportingContent = { Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingContent   = { Icon(icon, contentDescription = null) },
        trailingContent  = { Switch(checked = checked, onCheckedChange = onToggle) },
        modifier         = Modifier.clickable { onToggle(!checked) },
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f))
}

@Composable
private fun SettingsClickRow(icon: ImageVector, title: String, subtitle: String? = null, onClick: (() -> Unit)?) {
    ListItem(
        headlineContent  = { Text(title, fontWeight = FontWeight.Medium) },
        supportingContent = subtitle?.let { { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } },
        leadingContent   = { Icon(icon, contentDescription = null) },
        trailingContent  = if (onClick != null) { { Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) } } else null,
        modifier         = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f))
}

private fun AppTheme.displayName(): String = when (this) { AppTheme.LIGHT -> "Light"; AppTheme.DARK -> "Dark"; AppTheme.SYSTEM -> "System" }
private fun TextSize.displayName(): String = when (this) { TextSize.SMALL -> "Small"; TextSize.MEDIUM -> "Medium"; TextSize.LARGE -> "Large"; TextSize.EXTRA_LARGE -> "Extra Large" }
private fun TextSize.shortLabel(): String = when (this) { TextSize.SMALL -> "S"; TextSize.MEDIUM -> "M"; TextSize.LARGE -> "L"; TextSize.EXTRA_LARGE -> "XL" }
private fun StreamQuality.displayName(): String = when (this) { StreamQuality.LOW -> "Low"; StreamQuality.MEDIUM -> "Medium"; StreamQuality.HIGH -> "High" }

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun SettingsScreenV2Preview() {
    PocketNotesTheme {
        SettingsScreenV2(
            state = SettingsState(
                isLoading = false
            ), onAction = {})
    }
}