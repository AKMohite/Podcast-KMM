package com.mak.pocketnotes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform