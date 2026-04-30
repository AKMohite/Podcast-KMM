package com.mak.pocketnotes.android.feature.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText

class HomeRobot(private val rule: ComposeContentTestRule) {
    fun setContent(state: HomeScreenState) = apply {
        rule.setContent {
            HomeScreen(
                state = state,
                loadNextPodcasts = {},
                gotoDetails = {}
            )
        }
    }

    fun assertTrendingPodcastVisible(text: String) = apply {
        rule.onNodeWithText(text).assertIsDisplayed()
    }

    fun assertCuratedCategoryVisible(title: String)  = apply {
        rule.onNodeWithText(title).assertIsDisplayed()
    }

    fun assertCuratedPodcastVisible(text: String)  = apply {
        rule.onNodeWithText(text).assertIsDisplayed()
    }

}