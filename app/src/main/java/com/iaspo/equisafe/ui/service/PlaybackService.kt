package com.iaspo.equisafe.ui.service

import android.content.Intent
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService


class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.hasExtra(EXTRA_LINK)) {
            val linkVideo = intent.getStringExtra(EXTRA_LINK)
            val titleVideo = intent.getStringExtra(EXTRA_TITLE)
            initializeSessionAndPlayer(linkVideo, titleVideo)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initializeSessionAndPlayer(linkVideo: String?, titleVideo: String?) {
        val videoItem = MediaItem.Builder()
            .setUri(linkVideo)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(titleVideo)
                    .setArtist("Equisafe")
                    .build()
            ).build()

        val player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            exoPlayer.setMediaItem(videoItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }

        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        Log.d("Playback Service", "OnDestroy")
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    companion object {
        const val EXTRA_LINK = "extra_link"
        const val EXTRA_TITLE = "extra_title"
    }
}