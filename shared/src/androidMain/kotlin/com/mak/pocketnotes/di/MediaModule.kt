package com.mak.pocketnotes.di

import android.media.AudioAttributes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.mak.pocketnotes.service.media.notification.INotificationManager
import com.mak.pocketnotes.service.media.notification.PodtalkNotificationManager
import com.mak.pocketnotes.service.media.service.IServiceHandler
import com.mak.pocketnotes.service.media.service.PodtalkServiceHandler
import org.koin.dsl.module

@UnstableApi
val mediaModule = module {
    single<AudioAttributes> {
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
    }
    single<ExoPlayer> {
        ExoPlayer.Builder(get())
            .setAudioAttributes(get(), true)
            .setTrackSelector(DefaultTrackSelector(get()))
            .setHandleAudioBecomingNoisy(true)
            .build()
    }
    single<MediaSession> {
        MediaSession.Builder(get(), get()).build()
    }
    single<INotificationManager> { PodtalkNotificationManager(get(), get()) }
    single<IServiceHandler> { PodtalkServiceHandler(get()) }
}