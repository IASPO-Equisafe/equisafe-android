package com.iaspo.equisafe.ui.home.games.chronicles

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.ActivityGamesChroniclesBinding

class GamesChroniclesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamesChroniclesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGamesChroniclesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val fragmentManager = supportFragmentManager
        val introOneFragment = IntroOneFragment()
        val fragment = fragmentManager.findFragmentByTag(IntroOneFragment::class.java.simpleName)


        binding.btnPlayChronical.setOnClickListener {
            binding.toolbarDetail.gone()
            binding.linearLayout.gone()
            binding.frameContainer.visible()
            binding.frameLayout.background = null
            if (fragment !is IntroOneFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, introOneFragment, IntroOneFragment::class.java.simpleName)
                    .commit()
            }

        }
        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}