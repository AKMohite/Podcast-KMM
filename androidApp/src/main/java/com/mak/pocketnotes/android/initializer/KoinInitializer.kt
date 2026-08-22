package com.mak.pocketnotes.android.initializer

import android.content.Context
import androidx.startup.Initializer
import com.mak.pocketnotes.android.di.appModule
import com.mak.pocketnotes.di.getSharedModules
import com.mak.pocketnotes.di.mediaModule
import com.mak.pocketnotes.di.mediaModuleV2
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class KoinInitializer : Initializer<KoinApplication> {
  override fun create(context: Context): KoinApplication = startKoin {
    androidContext(context)
    modules(appModule + getSharedModules() + mediaModuleV2 + mediaModule)
  }

  override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}
