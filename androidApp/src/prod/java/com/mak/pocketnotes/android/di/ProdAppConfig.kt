package com.mak.pocketnotes.android.di

import com.mak.pocketnotes.core.common.utils.AppConfig

class ProdAppConfig : AppConfig {
  override val listenApiHost: String = "listen-api.listennotes.com"
  override val listenApiKey: String = SecretConstant.API_KEY
}
