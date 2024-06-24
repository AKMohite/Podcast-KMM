package com.mak.pocketnotes.data.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class IOSDispather: Dispatcher {
    override val io: CoroutineDispatcher
        get() = Dispatchers.Default
    override val computation: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}

internal actual fun provideDispatcher(): Dispatcher {
    return IOSDispather()
}