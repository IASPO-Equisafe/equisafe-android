package com.iaspo.equisafe.core.domain.usecase

import androidx.paging.PagingData
import com.iaspo.equisafe.core.data.network.response.homeresponse.FavoritesVideoItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.articleresponse.ArticlesItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.UserDataResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.gameresponse.QuestionsItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.mapresponse.MapResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.recomarticle.RecomendationArticle
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import kotlinx.coroutines.flow.Flow

interface HomeUseCase {
    fun getUserData(): Flow<ApiResult<UserDataResponse>>
    fun getUserFavoritesVideo(): Flow<ApiResult<List<FavoritesVideoItem>>>
    fun getArticles(): Flow<PagingData<ArticlesItem>>
    fun getGameChronicles(): Flow<ApiResult<List<QuestionsItem>>>
    fun getRecommendationArticle(idArticle: String): Flow<ApiResult<RecomendationArticle>>
    fun getMap(): Flow<ApiResult<MapResponse>>
}