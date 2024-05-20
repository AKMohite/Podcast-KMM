package com.mak.pocketnotes.service.media.service

import android.content.Intent
import android.os.Build
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.mak.pocketnotes.service.media.notification.INotificationManager
import org.koin.android.ext.android.inject

class MediaPlayerService: MediaSessionService() {

    private val mediaSession: MediaSession by inject()

    private val notificationManager: INotificationManager by inject()

    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.startNotification(
                mediaSession = mediaSession,
                mediaService = this
            )
        }
        return super.onStartCommand(intent, flags, startId)
    }

//    app removed from recent
//    override fun onTaskRemoved(rootIntent: Intent?) {
//        notificationManager.stopNotification()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            stopForeground(STOP_FOREGROUND_REMOVE)
//        }
//        stopSelf()
//        super.onTaskRemoved(rootIntent)
//    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        releaseSession()
        super.onDestroy()
    }

    private fun releaseSession() {
        mediaSession.apply {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
//                TODO check seek to save in preferences
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
            }
        }
    }
}