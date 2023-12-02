package com.iaspo.equisafe.core.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.response.learningresponse.VideosItem
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import kotlinx.coroutines.flow.Flow

interface ILearningRepository {
    fun getLearningVideo(query: String?): LiveData<PagingData<VideosItem>>
    fun saveLastSeenVideo(idVideo: String, linkVideo: String): Flow<ApiResult<CommonResponse>>

    fun deleteVideoFavorite(idVideo: String): Flow<ApiResult<CommonResponse>>
    fun addVideoFavorite(idVideo: String): Flow<ApiResult<CommonResponse>>
}