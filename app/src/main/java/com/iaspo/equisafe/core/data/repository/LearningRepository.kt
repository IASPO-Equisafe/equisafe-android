package com.iaspo.equisafe.core.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.iaspo.equisafe.core.adapter.LearningPagingSource
import com.iaspo.equisafe.core.constant.DataStoreConstant
import com.iaspo.equisafe.core.data.local.UserPreferences
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.response.learningresponse.VideosItem
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.data.network.retrofit.ApiService
import com.iaspo.equisafe.core.domain.model.PostData
import com.iaspo.equisafe.core.domain.model.RequestDelete
import com.iaspo.equisafe.core.domain.repository.ILearningRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlin.Exception

class LearningRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreferences
) : ILearningRepository {
    override fun getLearningVideo(query: String?): LiveData<PagingData<VideosItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                LearningPagingSource(apiService, dataStore, query)
            },
        ).liveData
    }

    override fun saveLastSeenVideo(idVideo: String, linkVideo: String): Flow<ApiResult<CommonResponse>> = flow {
        try {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
            }

            val response = apiService.saveLastSeenVideo(
                token = tokenUser ?: "",
                idVideo = idVideo
            )
            val responseBody = response.body()

            if (responseBody != null) {
                emit(ApiResult.Success(responseBody))
            } else {
                val gson = Gson()
                val errorResponse = response.errorBody()?.string()
                val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                emit(ApiResult.Error(errorMessage.message))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Message"))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteVideoFavorite(idVideo: String): Flow<ApiResult<CommonResponse>> = flow {
        try {
            emit(ApiResult.Loading())
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
            }

            val response = apiService.deleteUserFavoritesVideo(
                token = tokenUser ?: "",
                requestDelete = RequestDelete(
                    deleteAll = false,
                    idVideo = idVideo
                )
            )

            val responseBody = response.body()

            if (responseBody != null) {
                emit(ApiResult.Success(responseBody))
            } else {
                val gson = Gson()
                val errorResponse = response.errorBody()?.string()
                val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                emit(ApiResult.Error(errorMessage.message))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Message"))
        }
    }.flowOn(Dispatchers.IO)

    override fun addVideoFavorite(idVideo: String): Flow<ApiResult<CommonResponse>> = flow {
        try {
            emit(ApiResult.Loading())
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
            }

            val response = apiService.addUserFavoritesVideo(
                token = tokenUser ?: "",
                postData = PostData(idVideo = idVideo)
            )
            val responseBody = response.body()

            if (responseBody != null) {
                emit(ApiResult.Success(responseBody))
            } else {
                val gson = Gson()
                val errorResponse = response.errorBody()?.string()
                val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                emit(ApiResult.Error(errorMessage.message))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Message"))
        }
    }.flowOn(Dispatchers.IO)

}