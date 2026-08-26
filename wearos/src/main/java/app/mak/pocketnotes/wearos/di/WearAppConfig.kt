package app.mak.pocketnotes.wearos.di

import com.mak.pocketnotes.core.common.utils.AppConfig

class WearAppConfig : AppConfig {
  override val listenApiHost: String = "listen-api.listennotes.com"
  override val listenApiKey: String = "YOUR_PROD_API_KEY" // Or a specific Wear key
}
