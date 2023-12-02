package com.iaspo.equisafe.ui.learning

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.iaspo.equisafe.ui.service.PlaybackService
import com.iaspo.equisafe.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var linkVideoItem: String
    private lateinit var titleVideoItem: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()

        linkVideoItem = intent.getStringExtra(EXTRA_LINK) ?: ""
        titleVideoItem = intent.getStringExtra(EXTRA_TITLE) ?: ""

        val intent = Intent(this, PlaybackService::class.java)
        intent.putExtra(PlaybackService.EXTRA_LINK, linkVideoItem)
        intent.putExtra(PlaybackService.EXTRA_TITLE, titleVideoItem)
        startService(intent)
    }

    override fun onStart() {
        super.onStart()

        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            { binding.playerView.player = controllerFuture.get() },
            MoreExecutors.directExecutor()
        )

    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onDestroy() {
        val stopServiceIntent = Intent(this, PlaybackService::class.java)
        stopService(stopServiceIntent)

        super.onDestroy()
    }

    companion object {
        const val EXTRA_LINK = "extra_link"
        const val EXTRA_TITLE = "extra_title"
    }
}