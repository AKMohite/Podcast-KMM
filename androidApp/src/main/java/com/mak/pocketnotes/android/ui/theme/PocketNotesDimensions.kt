package com.mak.pocketnotes.android.ui.theme

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Layout tokens that adapt to window size class.
 * Wearable / Automotive consumers substitute their own implementations.
 */
data class PocketNotesDimensions(
    val screenHorizontalPadding: Dp,
    val cardMaxWidth: Dp,
    val gridColumns: Int,
    val carouselHeight: Dp,
    val podcastCardWidth: Dp,
    val useNavigationRail: Boolean,
    val usePermanentDrawer: Boolean,
    val listPaneWeight: Float,
    val detailPaneWeight: Float,
)

@Composable
fun dimensionTokens(): PocketNotesDimensions {
    val info = currentWindowAdaptiveInfoV2()
    val sizeClass = info.windowSizeClass
    return when {
        sizeClass.isExtraLarge() || sizeClass.isLarge() || sizeClass.isExpanded() ->PocketNotesDimensions(
            screenHorizontalPadding = 32.dp,
            cardMaxWidth            = 900.dp,
            gridColumns             = 4,
            carouselHeight          = 320.dp,
            podcastCardWidth        = 180.dp,
            useNavigationRail       = false,
            usePermanentDrawer      = true,
            listPaneWeight          = 0.35f,
            detailPaneWeight        = 0.65f,
        )
        sizeClass.isMedium() -> PocketNotesDimensions(
            screenHorizontalPadding = 24.dp,
            cardMaxWidth            = 600.dp,
            gridColumns             = 3,
            carouselHeight          = 260.dp,
            podcastCardWidth        = 160.dp,
            useNavigationRail       = true,
            usePermanentDrawer      = false,
            listPaneWeight          = 0.4f,
            detailPaneWeight        = 0.6f,
        )
        else /* Compact */ -> PocketNotesDimensions(
            screenHorizontalPadding = 16.dp,
            cardMaxWidth            = 400.dp,
            gridColumns             = 2,
            carouselHeight          = 200.dp,
            podcastCardWidth        = 148.dp,
            useNavigationRail       = false,
            usePermanentDrawer      = false,
            listPaneWeight          = 1f,
            detailPaneWeight        = 0f,
        )
    }
}