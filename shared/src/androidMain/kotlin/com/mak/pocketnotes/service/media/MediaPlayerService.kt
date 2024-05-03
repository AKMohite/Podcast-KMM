package com.mak.pocketnotes.service.media

import android.media.browse.MediaBrowser
import android.os.Bundle
import android.service.media.MediaBrowserService

class MediaPlayerService: MediaBrowserService() {
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(Constants.MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowser.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }
}