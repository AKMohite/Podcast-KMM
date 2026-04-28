package com.mak.pocketnotes.android.ui.theme

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXTRA_LARGE_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_LARGE_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

@Composable
fun adaptiveScreenInfo() = currentWindowAdaptiveInfoV2()


private fun WindowSizeClass.isCompact(): Boolean {
    return isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) || isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND)
}

fun WindowSizeClass.isExpanded(): Boolean {
    return isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
}

fun WindowSizeClass.isMedium(): Boolean {
    return isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
}

fun WindowSizeClass.isExtraLarge(): Boolean {
    return isWidthAtLeastBreakpoint(WIDTH_DP_EXTRA_LARGE_LOWER_BOUND)
}

fun WindowSizeClass.isLarge(): Boolean {
    return isWidthAtLeastBreakpoint(WIDTH_DP_LARGE_LOWER_BOUND)
}