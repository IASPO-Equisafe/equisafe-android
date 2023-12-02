package com.iaspo.equisafe.core.domain.usecase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.response.learningresponse.VideosItem
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.repository.ILearningRepository
import kotlinx.coroutines.flow.Flow

class LearningInteractor(private val learningRepository: ILearningRepository): LearningUseCase {
    override fun getVideoLearning(query: String?): LiveData<PagingData<VideosItem>> {
        Log.d("LearingIteractor", "observe")
        return learningRepository.getLearningVideo(query = query)
    }

    override fun addVideoFavorite(idVideo: String): Flow<ApiResult<CommonResponse>> {
        return learningRepository.addVideoFavorite(idVideo)
    }

    override fun deleteVideoFavorite(idVideo: String): Flow<ApiResult<CommonResponse>> {
        return learningRepository.deleteVideoFavorite(idVideo)
    }

    override fun saveLastSeenVideo(
        idVideo: String,
        linkVideo: String
    ): Flow<ApiResult<CommonResponse>> {
        return learningRepository.saveLastSeenVideo(idVideo, linkVideo)
    }
}