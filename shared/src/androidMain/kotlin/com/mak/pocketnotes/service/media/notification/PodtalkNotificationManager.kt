package com.mak.pocketnotes.service.media.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.mak.pocketnotes.service.media.Constants.PLAYBACK_NOTIFICATION_CHANNEL_ID
import com.mak.pocketnotes.service.media.Constants.PLAYBACK_NOTIFICATION_CHANNEL_NAME
import com.mak.pocketnotes.service.media.Constants.PLAYBACK_NOTIFICATION_ID

@UnstableApi
internal class PodtalkNotificationManager(
    private val context: Context,
    private val exoPlayer: Player
): INotificationManager {

    private val notificationManager = NotificationManagerCompat.from(context)
    private var mediaNotificationAdapter: PodtalkNotificationAdapter? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    override fun startNotification(
        mediaService: MediaSessionService,
        mediaSession: MediaSession
    ) {
        buildNotification(mediaSession)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundNotificationService(mediaService)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundNotificationService(mediaSessionService: MediaSessionService) {
        val notification = Notification.Builder(context, PLAYBACK_NOTIFICATION_CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        mediaSessionService.startForeground(PLAYBACK_NOTIFICATION_ID, notification)
    }

    @UnstableApi
    override fun stopNotification() {
        mediaNotificationAdapter?.onDestroy()
    }

    @UnstableApi
    private fun buildNotification(
        mediaSession: MediaSession
    ) {
        val adapter = PodtalkNotificationAdapter(
            context = context,
            pendingIntent = mediaSession.sessionActivity
        )
        mediaNotificationAdapter = adapter
        PlayerNotificationManager.Builder(
            context,
            PLAYBACK_NOTIFICATION_ID,
            PLAYBACK_NOTIFICATION_CHANNEL_ID
        ).setMediaDescriptionAdapter(adapter)
            .setSmallIconResourceId(androidx.media3.session.R.drawable.media_session_service_notification_ic_music_note)
            .build()
            .also {
                it.setMediaSessionToken(mediaSession.platformToken)
                it.setUseFastForwardActionInCompactView(true)
                it.setUseRewindActionInCompactView(true)
                it.setUseNextActionInCompactView(true)
                it.setPriority(NotificationCompat.PRIORITY_LOW)
                it.setPlayer(exoPlayer)
            }
    }
    private fun createNotificationChannel() {
        if (
            Build.VERSION.SDK_INT < 26 ||
            notificationManager.getNotificationChannel(PLAYBACK_NOTIFICATION_CHANNEL_ID) != null
        ) {
            return
        }
        val channel = NotificationChannel(
            PLAYBACK_NOTIFICATION_CHANNEL_ID,
            PLAYBACK_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "This notification is for media playback"
        }
        notificationManager.createNotificationChannel(channel)
    }

}

interface INotificationManager {
    fun startNotification(
        mediaService: MediaSessionService,
        mediaSession: MediaSession
    )

    fun stopNotification()
}