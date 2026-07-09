package com.mak.pocketnotes.core.common.di

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.coroutines.provideDispatcher
import org.koin.dsl.module

val commonModule = module {
    single<DispatcherProvider> { provideDispatcher() }
}