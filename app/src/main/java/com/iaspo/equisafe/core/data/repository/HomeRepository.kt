package com.iaspo.equisafe.core.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.iaspo.equisafe.core.adapter.ArticlePagingSource
import com.iaspo.equisafe.core.constant.DataStoreConstant
import com.iaspo.equisafe.core.data.local.UserPreferences
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.FavoritesVideoItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.articleresponse.ArticlesItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.UserDataResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.gameresponse.QuestionsItem
import com.iaspo.equisafe.core.data.network.response.homeresponse.mapresponse.MapResponse
import com.iaspo.equisafe.core.data.network.response.homeresponse.recomarticle.RecomendationArticle
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.data.network.retrofit.ApiService
import com.iaspo.equisafe.core.domain.repository.IHomeRepository
import com.iaspo.equisafe.core.utils.withNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class HomeRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreferences
): IHomeRepository {

    override fun getUserData(): Flow<ApiResult<UserDataResponse>> = flow {
        val dataToken = withContext(Dispatchers.IO) {
            dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull().toString()
        }

        dataToken.withNotNull { token ->
            try {
                emit(ApiResult.Loading())
                val response = apiService.getUserData(token = token)
                val responseBody = response.body()

                if (response.code() == 200 && responseBody != null) {
                    dataStore.saveStringData(responseBody.data?.username.toString(), DataStoreConstant.USERNAME)
                    dataStore.saveStringData(responseBody.data?.emergencyContact?.name.toString(), DataStoreConstant.EMERGENCY_NAME)
                    dataStore.saveStringData(responseBody.data?.emergencyContact?.noTelp.toString(), DataStoreConstant.EMERGENCY_NUMBER)
                    emit(ApiResult.Success(responseBody))
                } else if(responseBody.isNull && response.code() != 400){
                    emit(ApiResult.Empty)
                } else {
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message))
                }

            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    override fun getUserFavoritesVideo(): Flow<ApiResult<List<FavoritesVideoItem>>> = flow {
        val dataToken = withContext(Dispatchers.IO) {
            dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull().toString()
        }

        dataToken.withNotNull { token ->
            try {
                emit(ApiResult.Loading())
                val response = apiService.getUserData(token = token)
                val responseBody = response.body()
                val listFavoritesVideo = responseBody?.data?.favoritesVideo

                if (listFavoritesVideo?.isEmpty() == true) {
                    emit(ApiResult.Empty)
                    Log.d("HomeRepository", "IsEmpty")
                } else if (listFavoritesVideo != null){
                    emit(ApiResult.Success(listFavoritesVideo))
                    Log.d("HomeRepository", "IsSuccess")
                } else {
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message))
                }

            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getArticles(): Flow<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                ArticlePagingSource(apiService, dataStore)
            },
        ).flow
    }

    override fun getGameChronicles(): Flow<ApiResult<List<QuestionsItem>>> = flow {
        val dataToken = withContext(Dispatchers.IO) {
            dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull().toString()
        }

        dataToken.withNotNull { token ->
            try {
                emit(ApiResult.Loading())
                val response = apiService.getChroniclesGame(token = token, numberQuestion = 5)
                val responseBody = response.body()
                val listQuestion = responseBody?.data?.questions

                if (listQuestion?.isEmpty() == true) {
                    emit(ApiResult.Empty)
                    Log.d("HomeRepository", "IsEmpty")
                } else if (listQuestion != null){
                    emit(ApiResult.Success(listQuestion))
                    Log.d("HomeRepository", "IsSuccess")
                } else {
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message))
                }

            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getRecommendationArticle(idArticle: String): Flow<ApiResult<RecomendationArticle>> = flow {
        val dataSession = withContext(Dispatchers.IO){
            dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull().toString()
        }

        dataSession.withNotNull { token ->
            try {
                emit(ApiResult.Loading())
                val response = apiService.getArticlesRecommendation(
                    token = token,
                    idArticle = idArticle
                )

                val responseBody = response.body()?.data?.recomendationArticle

                if (response.code() == 200 && responseBody != null) {
                    emit(ApiResult.Success(responseBody))
                } else if(responseBody.isNull && response.code() != 400){
                    emit(ApiResult.Empty)
                } else {
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message))
                }
            }  catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    override fun getMap(): Flow<ApiResult<MapResponse>> = flow {
        val dataSession = withContext(Dispatchers.IO){
            dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull().toString()
        }

        dataSession.withNotNull { token ->
            try {
                emit(ApiResult.Loading())
                val response = apiService.getMaps(
                    token = token,
                    page = 1,
                    perPage = 99
                )

                val responseBody = response.body()

                if (response.code() == 200 && responseBody != null) {
                    emit(ApiResult.Success(responseBody))
                } else if(responseBody.isNull && response.code() != 400){
                    emit(ApiResult.Empty)
                } else {
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message))
                }
            }  catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

}