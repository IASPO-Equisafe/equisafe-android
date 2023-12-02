package com.iaspo.equisafe.ui.learning

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.model.LearningModel
import com.iaspo.equisafe.databinding.ActivityDetailLearningBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailLearningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLearningBinding
    private lateinit var linkVideo: String
    private lateinit var titleVideo: String
    private lateinit var idVideo: String

    private var isFavorite: Boolean = false

    private val learningViewModel: LearningViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setupAction()
    }


    private fun setupAction() {
        binding.imgDetailMaterial.setOnClickListener {
            saveLastSeenVideoUser()
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.iconFavorite.setOnClickListener {
            if (isFavorite) {
                binding.iconFavorite.setImageResource(R.drawable.icon_border_heart)
                binding.iconFavorite.setColorFilter(ContextCompat.getColor(this, R.color.colorTextRed))
                learningViewModel.deleteVideoFavorite(idVideo = idVideo).observe(this) {
                    it?.let { response ->
                        when(response) {
                            is ApiResult.Loading -> {

                            }

                            is ApiResult.Success -> {
                                Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT).show()
                            }

                            is ApiResult.Error -> {
                                Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                binding.iconFavorite.setImageResource(R.drawable.icon_favorite)
                binding.iconFavorite.setColorFilter(ContextCompat.getColor(this, R.color.colorTextRed))
                learningViewModel.addVideoFavorite(idVideo = idVideo).observe(this) {
                    it?.let { response ->
                        when(response) {
                            is ApiResult.Loading -> {

                            }

                            is ApiResult.Success -> {
                                Toast.makeText(this, "Success add to favorite", Toast.LENGTH_SHORT).show()
                            }

                            is ApiResult.Error -> {
                                Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goToVideo() {
        val intent = Intent(this@DetailLearningActivity, PlayerActivity::class.java)
        intent.putExtra(PlayerActivity.EXTRA_LINK, linkVideo)
        intent.putExtra(PlayerActivity.EXTRA_TITLE, titleVideo)
        startActivity(intent)
    }

    private fun saveLastSeenVideoUser() {
        learningViewModel.saveLastSeenVideo(idVideo = idVideo, linkVideo).observe(this){
            it?.let { response ->
                when(response) {
                    is ApiResult.Success -> {
                        Toast.makeText(this, "Enjoy", Toast.LENGTH_SHORT).show()
                        goToVideo()
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initView() {
        val dataDetail = if (Build.VERSION.SDK_INT >= 33) {
            intent.extras?.getParcelable(EXTRA_DATA, LearningModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.extras?.getParcelable(EXTRA_DATA)
        }

        binding.tvDetailTitle.text = dataDetail?.title
        binding.tvDetailDesc.text = dataDetail?.description

        Glide.with(this)
            .load(dataDetail?.thumbnailLink)
            .into(binding.imgDetailMaterial)

        linkVideo = dataDetail?.link ?: ""
        titleVideo = dataDetail?.title ?: ""
        idVideo = dataDetail?.id ?: ""
        isFavorite = dataDetail?.isFavorite ?: false

        if (isFavorite) {
            binding.iconFavorite.setImageResource(R.drawable.icon_favorite)
            binding.iconFavorite.setColorFilter(ContextCompat.getColor(this, R.color.colorTextRed))
        } else {
            binding.iconFavorite.setImageResource(R.drawable.icon_border_heart)
            binding.iconFavorite.setColorFilter(ContextCompat.getColor(this, R.color.colorTextRed))
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}