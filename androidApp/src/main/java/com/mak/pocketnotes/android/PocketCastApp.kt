package com.mak.pocketnotes.android

import android.app.Application
import com.mak.pocketnotes.android.di.appModule
import com.mak.pocketnotes.di.getSharedModules
import com.mak.pocketnotes.di.mediaModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class PocketCastApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PocketCastApp)
            modules(appModule + getSharedModules() + mediaModule)
        }
    }
}