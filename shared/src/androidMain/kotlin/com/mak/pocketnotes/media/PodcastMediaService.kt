package com.mak.pocketnotes.media

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import org.koin.android.ext.android.inject

class PodcastMediaService : MediaLibraryService() {

    private val player: Player by inject()

    private var session: MediaLibrarySession? = null

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        // TODO handle media session events
//        setMediaNotificationProvider(
//            DefaultMediaNotificationProvider.Builder(this)
//                .setNotificationId(NOTIFICATION_ID)
//                .setChannelId(NotificationDeepLink.CHANNEL_PLAYBACK_ID)
//                .setChannelName(R.string.notifcation_channel)
//                .build()
//        )
//        session = MediaLibrarySession
//            .Builder(this, player, SessionCallback())
//            .setSessionActivity(NotificationDeepLink.playerPendingIntent(this))
//            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = session

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (!player.playWhenReady || player.mediaItemCount == 0) stopSelf()
    }

    override fun onDestroy() {
        session?.run { player.release(); release() }
        session = null
        super.onDestroy()
    }

    private class SessionCallback : MediaLibrarySession.Callback {
        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>,
        ): ListenableFuture<List<MediaItem>> = Futures.immediateFuture(mediaItems)
    }

    companion object { const val NOTIFICATION_ID = 1001 }
}
