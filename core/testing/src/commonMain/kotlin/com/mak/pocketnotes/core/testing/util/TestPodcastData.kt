package com.mak.pocketnotes.core.testing.util

import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.home.models.SectionPodcast

object TestPodcastData {
  val samplePodcast = Podcast(
    id = "p1",
    title = "Android Developers Backstage",
    description = "Behind the scenes with the Android Engineering team",
    image = "https://example.com/p1.png",
    thumbnail = "https://example.com/p1_thumb.png",
    publisher = "Android",
    genres = "Technology, Android"
  )

  val samplePodcastList = listOf(
    samplePodcast,
    Podcast(
      id = "p2",
      title = "Kotlin By Example",
      description = "Deep dives into Kotlin programming",
      image = "https://example.com/p2.png",
      thumbnail = "https://example.com/p2_thumb.png",
      publisher = "JetBrains",
      genres = "Programming, Kotlin"
    )
  )

  val sampleEpisode = PodcastEpisode(
    id = "ep1",
    podcastId = "p1",
    title = "Episode 100: Compose KMP",
    description = "Discussion on Jetpack Compose and KMP",
    image = "https://example.com/ep1.png",
    listennotesUrl = "https://example.com/ep1",
    thumbnail = "https://example.com/ep1_thumb.png",
    uploadedAt = 1600000000000L,
    audio = "https://example.com/ep1.mp3",
    duration = 1800
  )

  val sampleCuratedPodcast = CuratedPodcast(
    id = "c1",
    title = "Top Tech Shows",
    description = "Curated tech podcasts",
    podcasts = listOf(
      SectionPodcast(
        id = "p1",
        title = "Android Developers Backstage",
        image = "https://example.com/p1.png",
        thumbnail = "https://example.com/p1_thumb.png",
        publisher = "Android"
      )
    )
  )
}
