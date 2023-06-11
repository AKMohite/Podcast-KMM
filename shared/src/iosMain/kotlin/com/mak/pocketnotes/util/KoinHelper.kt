package com.mak.pocketnotes.util

import com.mak.pocketnotes.di.getSharedModules
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(getSharedModules())
    }
}