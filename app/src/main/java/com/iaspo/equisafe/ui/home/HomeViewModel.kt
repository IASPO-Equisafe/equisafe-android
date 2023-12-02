package com.iaspo.equisafe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.iaspo.equisafe.core.data.network.response.homeresponse.FavoritesVideoItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.articleresponse.ArticlesItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.UserDataResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.gameresponse.QuestionsItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.recomarticle.RecomendationArticle
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.usecase.HomeUseCase

class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {
    fun getUserData(): LiveData<ApiResult<UserDataResponse>> {
        return homeUseCase.getUserData().asLiveData()
    }

    fun getUserFavoritesVideo(): LiveData<ApiResult<List<FavoritesVideoItem>>> {
        return homeUseCase.getUserFavoritesVideo().asLiveData()
    }
    fun getChroniclesGame(): LiveData<ApiResult<List<QuestionsItem>>> {
        return homeUseCase.getGameChronicles().asLiveData()
    }

    fun getArticles(): LiveData<PagingData<ArticlesItem>> {
        return homeUseCase.getArticles().asLiveData()
    }

    fun getRecommendationArticles(idArticle: String): LiveData<ApiResult<RecomendationArticle>> {
        return homeUseCase.getRecommendationArticle(idArticle).asLiveData()
    }
}