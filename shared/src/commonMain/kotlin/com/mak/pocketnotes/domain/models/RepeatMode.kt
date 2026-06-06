package com.mak.pocketnotes.domain.models

enum class RepeatMode {
    NONE,   // no repeat
    ONE,    // loop current episode
    ALL;    // loop whole queue

    fun next(): RepeatMode = entries[(ordinal + 1) % entries.size]
}

val PLAYBACK_SPEEDS = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f)