package com.mak.pocketnotes.android.feature.discover

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.mak.pocketnotes.android.R

internal fun discoverRobot(rule: ComposeContentTestRule, block: DiscoverRobot.() -> Unit) {
  DiscoverRobot(rule).block()
}

internal class DiscoverRobot(
  private val rule: ComposeContentTestRule
) {
  private val context = InstrumentationRegistry.getInstrumentation().targetContext

  fun setContent(
    state: DiscoverScreenState,
    gotoDetails: (String) -> Unit = {},
    refreshPodcasts: () -> Unit = {},
    onErrorConsumed: () -> Unit = {}
  ) = apply {
    rule.setContent {
      DiscoverScreen(
        state = state,
        gotoDetails = gotoDetails,
        refreshPodcasts = refreshPodcasts,
        onErrorConsumed = onErrorConsumed
      )
    }
  }

  fun assertShimmerVisible() = apply {
    rule.onNodeWithTag(DiscoverScreenTestTag.SHIMMER).assertIsDisplayed()
  }

  fun assertShimmerDoesNotExist() = apply {
    rule.onNodeWithTag(DiscoverScreenTestTag.SHIMMER).assertDoesNotExist()
  }

  fun assertContentVisible() = apply {
    rule.onNodeWithTag(DiscoverScreenTestTag.CONTENT).assertIsDisplayed()
  }

  fun assertContentDoesNotExist() = apply {
    rule.onNodeWithTag(DiscoverScreenTestTag.CONTENT).assertDoesNotExist()
  }

  fun assertPodcastVisible(title: String) = apply {
    rule.onAllNodesWithText(title).onFirst().assertIsDisplayed()
  }

  fun assertRetryVisible() = apply {
    rule.onNodeWithText(context.getString(R.string.action_retry)).assertIsDisplayed()
  }

  fun clickRetry() = apply {
    rule.onNodeWithText(context.getString(R.string.action_retry)).performClick()
  }
}
