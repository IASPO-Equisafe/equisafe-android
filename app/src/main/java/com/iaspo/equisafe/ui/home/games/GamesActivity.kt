package com.iaspo.equisafe.ui.home.games

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.iaspo.equisafe.R
import com.iaspo.equisafe.databinding.ActivityGamesBinding
import com.iaspo.equisafe.ui.home.games.chronicles.GamesChroniclesActivity

class GamesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intiView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        binding.imgMaterial.setOnClickListener {
            startActivity(Intent(this@GamesActivity, GamesChroniclesActivity::class.java))
        }

        binding.cardGame1.cardGameEquisafe.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
        }

        binding.cardGame2.cardGameEquisafe.setOnClickListener {
            startActivity(Intent(this@GamesActivity, GamesChroniclesActivity::class.java))
        }
    }

    private fun intiView() {
        binding.cardGame1.ivCardGames.setImageResource(R.drawable.game1_circle)
        binding.cardGame2.ivCardGames.setImageResource(R.drawable.game2_circle)

        binding.cardGame1.tvTitleCardGames.text = getString(R.string.disaster_quest)
        binding.cardGame2.tvTitleCardGames.text = getString(R.string.crisis_chronicles)

        binding.cardGame1.tvDescCardGame.text = getString(R.string.protect_learn_and_triumph_over_disasters)
        binding.cardGame2.tvDescCardGame.text = getString(R.string.unraveling_nature_s_fury_one_chapter_at_a_time)

        binding.cardGame1.tvStarRating.text = "5,0"
        binding.cardGame2.tvStarRating.text = "4,8"
    }
}