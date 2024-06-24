package com.mak.pocketnotes.data.util

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatcher {
    val io: CoroutineDispatcher
    val computation: CoroutineDispatcher
    val main: CoroutineDispatcher
}

internal expect fun provideDispatcher(): Dispatcher