package com.iaspo.equisafe.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.adapter.ViewBindingSampleAdapter
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.model.LearningModel
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.invisible
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.FragmentHomeBinding
import com.iaspo.equisafe.ui.learning.DetailLearningActivity
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.indicator.IndicatorView
import com.zhpan.indicator.enums.IndicatorSlideMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var mViewPager: BannerViewPager<Int>
    private lateinit var indicatorView: IndicatorView
    private var mPictureList: MutableList<Int> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewPager = view.findViewById(R.id.banner_view)
        indicatorView = view.findViewById(R.id.indicator_view)
        initBVP()
        initAction()
        initView()
    }

    private fun initAction() {
        binding.btnArticles.setOnClickListener {
            val toArticleActivity = HomeFragmentDirections.actionNavigationHomeToArticleActivity()
            it.findNavController().navigate(toArticleActivity)
        }

        binding.btnGames.setOnClickListener {
            val toGamesActivity = HomeFragmentDirections.actionNavigationHomeToGamesActivity()
            it.findNavController().navigate(toGamesActivity)
        }

        binding.btnFavorite.setOnClickListener {
            val toFavoriteActivity = HomeFragmentDirections.actionNavigationHomeToFavoritesActivity()
            it.findNavController().navigate(toFavoriteActivity)
        }
    }

    private fun initView() {
        homeViewModel.getUserData().observe(viewLifecycleOwner){
            Log.d("HomeFragment", "InitView")
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        binding.cardLp.invisible()
                        binding.lottieLoadingCard.visible()
                        binding.lottieLoadingGreeting.visible()
                        binding.greeting.invisible()
                    }

                    is ApiResult.Success -> {
                        binding.cardLp.visible()
                        binding.lottieLoadingCard.gone()
                        binding.lottieLoadingGreeting.gone()
                        binding.greeting.visible()

                        val lastVideo: Boolean

                        binding.greeting.text = getString(R.string.greetings_home, response.data.data?.username)
                        if (response.data.data?.lastVideo?.id.isNullOrEmpty()) {
                            binding.imgMaterial.setImageResource(R.drawable.empty_lastvid)
                            binding.cardLp.elevation = 2F
                            lastVideo = false

                        } else {
                            Glide.with(this@HomeFragment)
                                .load(response.data.data?.lastVideo?.thumbnailLink)
                                .into(binding.imgMaterial)
                            binding.cardLp.elevation = 4f
                            lastVideo = true
                            Log.d("HomeFragment", "LastVideo not null")
                        }

                        binding.cardLp.setOnClickListener {
                            if (lastVideo) {
                                val intent = Intent(requireContext(), DetailLearningActivity::class.java)

                                val extraData = LearningModel(
                                    id = response.data.data?.lastVideo?.id!!,
                                    title = response.data.data.lastVideo.title!!,
                                    description = response.data.data.lastVideo.description!!,
                                    link = response.data.data.lastVideo.link!!,
                                    thumbnailLink = response.data.data.lastVideo.thumbnailLink!!,
                                    isFavorite = response.data.data.lastVideo.isFavorite!!
                                )
                                intent.putExtra(DetailLearningActivity.EXTRA_DATA, extraData)
                                startActivity(intent)
                            } else {
                                Toast.makeText(requireContext(), "You've never watched any video", Toast.LENGTH_LONG).show()
                            }

                        }
                    }

                    is ApiResult.Error -> {
                        binding.cardLp.invisible()
                        binding.lottieLoadingCard.visible()
                        binding.lottieLoadingGreeting.visible()
                        binding.greeting.invisible()
                        Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_LONG).show()
                    }

                    is ApiResult.Empty -> {
                        binding.cardLp.visible()
                        binding.lottieLoadingCard.gone()
                        binding.lottieLoadingGreeting.gone()
                        binding.greeting.visible()
                    }
                }
            }
        }
    }

    private fun initBVP() {
        mViewPager.apply {
            registerLifecycleObserver(lifecycle)
            adapter = ViewBindingSampleAdapter()
            setIndicatorSliderColor(
                getColor(requireContext(), R.color.black_40),
                getColor(requireContext(), R.color.green_main)
            )
            setIndicatorVisibility(View.GONE)
            setIndicatorSlideMode(IndicatorSlideMode.WORM)
            setIndicatorSliderGap(16)
            setPageMargin(20)
            setRevealWidth(-10)
            setIndicatorView(indicatorView)
            setInterval(5000)
            create(getPickList())
        }
        mViewPager.removeDefaultPageTransformer()
    }

    @SuppressLint("DiscouragedApi")
    private fun getPickList(): MutableList<Int> {
        mPictureList.clear()
        for (i in 1..3) {
            val drawable = resources.getIdentifier("banner$i", "drawable", requireContext().packageName)
            mPictureList.add(drawable)
        }
        return mPictureList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}