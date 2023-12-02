package com.iaspo.equisafe.ui.auth.splashscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.iaspo.equisafe.MainActivity
import com.iaspo.equisafe.databinding.ActivitySplashScreenBinding
import com.iaspo.equisafe.ui.auth.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val splashScreenViewModel: SplashScreenViewModel by viewModel()

    companion object {
        private const val SPLASH_TIME_OUT: Long = 3000

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAnimation()
        setupAction()
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

    private fun setupAction() {
        lifecycleScope.launch(Dispatchers.IO){
            delay(SPLASH_TIME_OUT)
            withContext(Dispatchers.Main) {
                checkUserSession()
            }
        }
    }

    private fun checkUserSession() {
        splashScreenViewModel.getUserSession().observe(this){
            if (it.isNotEmpty()) {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupAnimation() {
        val translationAnimator = ObjectAnimator.ofFloat(binding.mainLogo, View.TRANSLATION_Y, 0f, -50f).apply {
            duration = 2000
        }

        val alphaAnimator = ObjectAnimator.ofFloat(binding.mainLogo, View.ALPHA,  1f).apply {
            duration = 2000
        }

        val ellipse1 = ObjectAnimator.ofFloat(binding.ellipse1, View.ALPHA, 1f).apply {
            duration = 200
        }

        val ellipse2 = ObjectAnimator.ofFloat(binding.ellipse2, View.ALPHA, 1f).apply {
            duration = 200
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationAnimator, alphaAnimator, ellipse1, ellipse2)
        animatorSet.start()
    }

    private fun setupViewModel() {

    }
}