package com.mak.pocketnotes.di

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.mak.pocketnotes.media.QueueManager
import com.mak.pocketnotes.service.media.notification.INotificationManager
import com.mak.pocketnotes.service.media.notification.PodtalkNotificationManager
import com.mak.pocketnotes.service.media.service.IServiceHandler
import com.mak.pocketnotes.service.media.service.PodtalkServiceHandler
import org.koin.dsl.module

@UnstableApi
val mediaModule = module {
    single<INotificationManager> { PodtalkNotificationManager(get(), get()) }
    single<MediaSession> {
        MediaSession.Builder(get(), get()).build()
    }
    single<IServiceHandler> { PodtalkServiceHandler(get()) }
}

val mediaModuleV2 = module {
    single { QueueManager() }
    single<Player> {
        ExoPlayer.Builder(get())
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
                    .setUsage(C.USAGE_MEDIA).build(),
                true,
            )
            .setHandleAudioBecomingNoisy(true)
            .setSeekBackIncrementMs(10_000L)
            .setSeekForwardIncrementMs(30_000L)
            .build()
    }
}