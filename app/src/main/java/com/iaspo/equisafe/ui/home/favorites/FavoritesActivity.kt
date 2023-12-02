package com.iaspo.equisafe.ui.home.favorites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.iaspo.equisafe.core.adapter.FavoriteAdapter
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.ActivityFavoritesBinding
import com.iaspo.equisafe.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding

    private val homeViewModel: HomeViewModel by viewModel()

    private var initialData: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initialData = true

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!initialData) {
            initRecyclerView()
            initialData = true
        }
    }

    override fun onPause() {
        super.onPause()
        initialData = false
    }

    private fun initRecyclerView() {
        homeViewModel.getUserFavoritesVideo().observe(this) {
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visible()
                    }

                    is ApiResult.Success -> {
                        binding.lottieLoading.gone()
                        binding.rvFavVideo.layoutManager = LinearLayoutManager(this)
                        binding.rvFavVideo.setHasFixedSize(true)
                        binding.rvFavVideo.adapter = FavoriteAdapter(response.data)
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoading.gone()
                        Toast.makeText(this, response.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    is ApiResult.Empty -> {
                        binding.rvFavVideo.gone()
                        binding.lottieLoading.gone()
                        binding.emptyText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}