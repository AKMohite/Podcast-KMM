package com.mak.pocketnotes.core.widget.di

import com.mak.pocketnotes.core.common.WidgetUpdater
import com.mak.pocketnotes.core.widget.GlanceWidgetUpdater
import org.koin.dsl.module

val widgetModule = module {
  single<WidgetUpdater> { GlanceWidgetUpdater(get()) }
}
