package com.iaspo.equisafe.ui.home.article

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.iaspo.equisafe.core.data.network.response.homeresponse.articleresponse.ArticlesItem
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.ActivityDetailArticleBinding
import com.iaspo.equisafe.ui.home.HomeViewModel
import com.iaspo.equisafe.ui.learning.DetailLearningActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    private lateinit var idArticle: String
    private lateinit var articleItem: ArticlesItem

    private val homeViewModel: HomeViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.cardArticle.setOnClickListener {
            val intent = Intent(this, DetailArticleActivity::class.java)
            intent.putExtra(EXTRA_DATA, articleItem)
            startActivity(intent)
        }
    }

    private fun initView() {
        val dataDetail = if (Build.VERSION.SDK_INT >= 33) {
            intent.extras?.getParcelable(
                EXTRA_DATA,
                ArticlesItem::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.extras?.getParcelable(DetailLearningActivity.EXTRA_DATA)
        }

        dataDetail?.let { data ->
            idArticle = data.id
            binding.apply {
                tvDetailTitleArticle.text = data.title
                dateDetailArticle.text = data.createdAt
                descDetailArticle.text = data.content
            }

            Glide.with(this)
                .load(data.pic)
                .into(binding.ivDetailArticle)
        }

        recommendationArticle()
    }

    private fun recommendationArticle() {
        homeViewModel.getRecommendationArticles(idArticle).observe(this) {
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visible()
                    }

                    is ApiResult.Success -> {
                        binding.lottieLoading.gone()
                        binding.cardTitleArticle.text = response.data.title
                        binding.cardDescriptionArticle.text = response.data.content
                        binding.cardDateArticle.text = response.data.createdAt
                        Glide.with(this@DetailArticleActivity)
                            .load(response.data.pic)
                            .into(binding.cardImgArticle)

                        articleItem = ArticlesItem(
                            author = response.data.author ?: "",
                            id = response.data.id ?: "",
                            source = response.data.source ?: "",
                            pic = response.data.pic ?: "",
                            title = response.data.title ?: "",
                            content = response.data.content ?: "",
                            createdAt = response.data.createdAt ?: ""
                        )
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoading.gone()
                        Toast.makeText(this, response.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    is ApiResult.Empty -> {
                        binding.lottieLoading.gone()
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}