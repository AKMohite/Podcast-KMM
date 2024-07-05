package com.mak.pocketnotes.service.media.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
class PodtalkNotificationAdapter(
    private val context: Context,
    private val pendingIntent: PendingIntent?
): PlayerNotificationManager.MediaDescriptionAdapter {

    private var currentMediaImage: String? = null
    private var currentBitmap: Bitmap? = null
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    override fun getCurrentContentTitle(player: Player): CharSequence {
        return player.mediaMetadata.albumTitle ?: "Unknown"
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? = pendingIntent

    override fun getCurrentContentText(player: Player): CharSequence? =
        player.mediaMetadata.displayTitle ?: "Unknown"

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        val image = player.mediaMetadata.artworkUri.toString()
        return if (currentMediaImage != image) {

            // Cache the bitmap for the current song so that successive calls to
            // `getCurrentLargeIcon` don't cause the bitmap to be recreated.
            currentMediaImage = image
            serviceScope.launch {
                currentBitmap = resolveBitmap(image)
                currentBitmap?.let { callback.onBitmap(it) }
            }
            currentBitmap
        } else {
            currentBitmap
        }
    }

    private suspend fun resolveBitmap(image: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(image)
                .allowHardware(false) // Disable hardware bitmaps.
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
            val result = (loader.execute(request) as? SuccessResult)?.drawable
            (result as? BitmapDrawable)?.bitmap
        }
    }

    fun onDestroy() {
        serviceJob.cancel()
    }
}