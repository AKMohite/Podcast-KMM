package com.mak.pocketnotes.android.feature.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.mak.pocketnotes.android.common.navigation.AdaptiveScreenType
import com.mak.pocketnotes.utils.sample.sampleCuratedPodcasts
import com.mak.pocketnotes.utils.sample.samplePodcasts
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadPodcasts() {
        composeTestRule.setContent {
            HomeScreen(
                state = HomeScreenState(
                    podcasts = samplePodcasts,
                    topPodcasts = samplePodcasts.takeLast(3),
                    curatedPodcasts = sampleCuratedPodcasts
                ),
                loadNextPodcasts = {},
                gotoDetails = {},
                adaptiveScreenType = AdaptiveScreenType.Compact
            )
        }

        composeTestRule.onNodeWithText(samplePodcasts[0].title).assertIsDisplayed()
    }
}