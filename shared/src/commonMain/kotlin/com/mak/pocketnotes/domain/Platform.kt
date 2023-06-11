package com.mak.pocketnotes.domain

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform