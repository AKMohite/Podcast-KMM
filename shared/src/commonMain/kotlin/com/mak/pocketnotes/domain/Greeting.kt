package com.mak.pocketnotes.domain

class Greeting {
  private val platform: Platform = getPlatform()

  fun greeting(): String = "Hello, ${platform.name}!"
}
