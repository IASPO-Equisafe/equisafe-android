package com.iaspo.equisafe.ui.home.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.iaspo.equisafe.core.adapter.ArticleAdapter
import com.iaspo.equisafe.core.adapter.LoadingStateAdapter
import com.iaspo.equisafe.databinding.ActivityArticleBinding
import com.iaspo.equisafe.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
    }

    private fun initView() {
        binding.rvArticle.layoutManager = LinearLayoutManager(this)
        val adapter = ArticleAdapter()

        binding.rvArticle.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        homeViewModel.getArticles().observe(this){
            adapter.submitData(lifecycle, it)
        }

    }
}