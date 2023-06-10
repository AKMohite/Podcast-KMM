package com.mak.pocketnotes.data.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


internal class AndroidDispatcher: Dispatcher {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
}
actual fun provideDispatcher(): Dispatcher {
    return AndroidDispatcher()
}