package com.iaspo.equisafe.core.domain.usecase

import androidx.paging.PagingData
import com.iaspo.equisafe.core.data.network.response.homeresponse.FavoritesVideoItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.articleresponse.ArticlesItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.UserDataResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.gameresponse.QuestionsItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.mapresponse.MapResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.recomarticle.RecomendationArticle
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow

class HomeInteractor(private val homeRepository: IHomeRepository) : HomeUseCase {

    override fun getUserData(): Flow<ApiResult<UserDataResponse>> {
        return homeRepository.getUserData()
    }

    override fun getUserFavoritesVideo(): Flow<ApiResult<List<FavoritesVideoItem>>> {
        return homeRepository.getUserFavoritesVideo()
    }

    override fun getArticles(): Flow<PagingData<ArticlesItem>> {
        return homeRepository.getArticles()
    }

    override fun getGameChronicles(): Flow<ApiResult<List<QuestionsItem>>> {
        return homeRepository.getGameChronicles()
    }

    override fun getRecommendationArticle(idArticle: String): Flow<ApiResult<RecomendationArticle>> {
        return homeRepository.getRecommendationArticle(idArticle)
    }

    override fun getMap(): Flow<ApiResult<MapResponse>> {
        return homeRepository.getMap()
    }
}