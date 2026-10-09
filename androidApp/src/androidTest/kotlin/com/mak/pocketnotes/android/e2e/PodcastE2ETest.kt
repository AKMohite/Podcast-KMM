package com.mak.pocketnotes.android.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mak.pocketnotes.android.AppMainActivity
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.utils.sample.samplePodcasts
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PodcastE2ETest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<AppMainActivity>()

  @Test
  fun testPodcastSubscriptionUserFlow() {
    val podcast = samplePodcasts[0]

    // 1. Launch app and verify discover screen loads with podcast title
    composeTestRule.waitUntil(timeoutMillis = 5000) {
      try {
        composeTestRule.onNodeWithText(podcast.title).assertIsDisplayed()
        true
      } catch (_: AssertionError) {
        false
      }
    }

    // 2. Click on podcast to open podcast detail screen
    composeTestRule.onNodeWithText(podcast.title).performClick()

    // 3. In podcast detail screen: Subscribe -> Unsubscribe -> Subscribe
    val subscribeText = composeTestRule.activity.getString(R.string.action_subscribe)
    val subscribedText = composeTestRule.activity.getString(R.string.action_subscribed)

    // Click subscribe button
    composeTestRule.onNodeWithText(subscribeText).performClick()
    composeTestRule.waitForIdle()

    // Verify subscribed state
    composeTestRule.onNodeWithText(subscribedText).assertIsDisplayed()

    // Unsubscribe
    composeTestRule.onNodeWithText(subscribedText).performClick()
    composeTestRule.waitForIdle()

    // Verify unsubscribed state (button shows "Subscribe")
    composeTestRule.onNodeWithText(subscribeText).assertIsDisplayed()

    // Subscribe again
    composeTestRule.onNodeWithText(subscribeText).performClick()
    composeTestRule.waitForIdle()

    // Verify subscribed state again
    composeTestRule.onNodeWithText(subscribedText).assertIsDisplayed()

    // 4. Go back to discover screen
    composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
    composeTestRule.waitForIdle()

    // Verify discover screen is loaded and podcast is visible again
    composeTestRule.onNodeWithText(podcast.title).assertIsDisplayed()

    // 5. Click on the same podcast again
    composeTestRule.onNodeWithText(podcast.title).performClick()
    composeTestRule.waitForIdle()

    // 6. Verify whether podcast is subscribed or unsubscribed (should be subscribed)
    composeTestRule.onNodeWithText(subscribedText).assertIsDisplayed()
  }
}
