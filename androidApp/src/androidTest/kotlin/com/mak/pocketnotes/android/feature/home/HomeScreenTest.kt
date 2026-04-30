package com.mak.pocketnotes.android.feature.home

import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.mak.pocketnotes.utils.sample.sampleCuratedPodcasts
import com.mak.pocketnotes.utils.sample.samplePodcasts
import org.junit.Rule
import org.junit.Test
import kotlin.getValue

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val robot by lazy { HomeRobot(composeTestRule) }

    @Test
    fun loadPodcasts() {
        val state = HomeScreenState(
            podcasts = samplePodcasts,
            topPodcasts = samplePodcasts.takeLast(3),
            curatedPodcasts = sampleCuratedPodcasts
        )
        robot.setContent(state)
            .assertTrendingPodcastVisible(samplePodcasts[0].title)
            .assertCuratedCategoryVisible(sampleCuratedPodcasts[0].title)
            .assertCuratedPodcastVisible(sampleCuratedPodcasts[0].podcasts[0].title)

    }
}
