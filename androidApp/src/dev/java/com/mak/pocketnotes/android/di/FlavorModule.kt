package com.mak.pocketnotes.android.di

import com.mak.pocketnotes.core.common.utils.AppConfig
import com.mak.pocketnotes.core.common.utils.appConfigQualifier
import org.koin.dsl.module

val flavorModule = module {
  single<AppConfig>(appConfigQualifier) { DevAppConfig() }
}
