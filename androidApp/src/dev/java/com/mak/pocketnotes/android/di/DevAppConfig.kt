package com.mak.pocketnotes.android.di

import com.mak.pocketnotes.core.common.utils.AppConfig

class DevAppConfig : AppConfig {
  override val listenApiHost: String = "listen-api-test.listennotes.com"
  override val listenApiKey: String = "YOUR_DEV_API_KEY"
}
