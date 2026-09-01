package com.mak.pocketnotes.core.common.utils

import org.koin.core.qualifier.named

interface AppConfig {
  val listenApiHost: String
  val listenApiKey: String
}

val appConfigQualifier = named("listenNotesQualifier")
