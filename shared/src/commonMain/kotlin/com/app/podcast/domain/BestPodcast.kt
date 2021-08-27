package com.app.podcast.domain

data class BestPodcast(
    val id: String,
    val title: String,
    val description: String,
    val link: String,
    val imgURL: String,
    val audioLink: String
)