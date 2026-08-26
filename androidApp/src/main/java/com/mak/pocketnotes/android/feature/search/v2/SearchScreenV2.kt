package com.mak.pocketnotes.android.feature.search.v2

import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.ui.theme.isMedium


fun EntryProviderScope<NavKey>.searchEntryV2(navigator: Navigator) {
  entry<Search> {
    SearchScreenV2()
  }
}

@Composable
internal fun SearchScreenV2() {
  val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
  when {
    sizeClass.isMedium() -> DockedSearchField()
    else -> FullscreenSearchbarField()
  }
}

@Composable
fun FullscreenSearchbarField() {
  Text(
    text = "Search full screen"
  )
}

@Composable
fun DockedSearchField() {
  Text(
    text = "Search docked"
  )
}
