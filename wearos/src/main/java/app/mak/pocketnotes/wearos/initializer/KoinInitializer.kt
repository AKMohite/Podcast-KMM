package app.mak.pocketnotes.wearos.initializer

import android.content.Context
import androidx.startup.Initializer
import app.mak.pocketnotes.wearos.di.WearAppConfig
import app.mak.pocketnotes.wearos.di.viewmodelModule
import com.mak.pocketnotes.core.common.di.commonModule
import com.mak.pocketnotes.core.common.utils.AppConfig
import com.mak.pocketnotes.core.common.utils.appConfigQualifier
import com.mak.pocketnotes.core.database.di.localModule
import com.mak.pocketnotes.core.feature.data.di.coreDataModule
import com.mak.pocketnotes.core.remote.di.ktorModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinInitializer : Initializer<KoinApplication> {
  override fun create(context: Context): KoinApplication = startKoin {
    androidContext(context)
    modules(
      listOf(
        flavorModule,
        viewmodelModule,
        commonModule,
        ktorModule(),
        localModule,
        coreDataModule
      )
    )
  }

  override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}

private val flavorModule = module {
  single<AppConfig>(appConfigQualifier) { WearAppConfig() }
}
