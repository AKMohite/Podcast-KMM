package com.mak.pocketnotes.core.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPodcastDTO(
  @SerialName("count")
  val count: Int? = null,
  @SerialName("next_offset")
  val nextOffset: Int? = null,
  @SerialName("results")
  val results: List<SearchPodcastResultDTO>? = null,
  @SerialName("took")
  val took: Double? = null,
  @SerialName("total")
  val total: Int? = null
)
