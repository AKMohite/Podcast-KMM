package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LookingForDTO(
    @SerialName("cohosts")
    val cohosts: Boolean,
    @SerialName("cross_promotion")
    val crossPromotion: Boolean,
    @SerialName("guests")
    val guests: Boolean,
    @SerialName("sponsors")
    val sponsors: Boolean
)