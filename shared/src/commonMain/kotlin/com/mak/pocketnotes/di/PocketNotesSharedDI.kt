package com.mak.pocketnotes.di

import com.mak.pocketnotes.core.common.di.commonModule
import com.mak.pocketnotes.core.database.di.localModule
import com.mak.pocketnotes.core.feature.data.di.coreDataModule
import com.mak.pocketnotes.core.remote.di.ktorModule
import com.mak.pocketnotes.domain.usecase.SearchPodcast
import org.koin.core.module.Module
import org.koin.dsl.module

private val domainModule = module {
    factory { SearchPodcast() }
}

internal expect fun platformModule(): Module

private val sharedModules =
    listOf(
        commonModule,
        ktorModule(),
        localModule,
        coreDataModule,
        domainModule,
        platformModule()
    )

fun getSharedModules() = sharedModules