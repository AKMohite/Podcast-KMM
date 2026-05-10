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
        assertTextIsDisplayed(text)
    }

    private fun assertTextIsDisplayed(text: String) {
        rule.onNodeWithText(text).assertIsDisplayed()
    }

    fun assertCuratedCategoryVisible(text: String)  = apply {
        assertTextIsDisplayed(text)
    }

    fun assertCuratedPodcastVisible(text: String)  = apply {
        assertTextIsDisplayed(text)
    }

}