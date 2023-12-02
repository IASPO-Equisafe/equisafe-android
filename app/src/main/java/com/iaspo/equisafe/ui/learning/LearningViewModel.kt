package com.iaspo.equisafe.ui.learning

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.response.learningresponse.VideosItem
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.usecase.LearningUseCase

class LearningViewModel(private val learningUseCase: LearningUseCase): ViewModel() {
    fun getVideoLearning(query: String?): LiveData<PagingData<VideosItem>> {
        Log.d("LearningViewModel", "observe")
        return learningUseCase.getVideoLearning(query)
    }

    fun addVideoFavorite(idVideo: String): LiveData<ApiResult<CommonResponse>> {
        return learningUseCase.addVideoFavorite(idVideo).asLiveData()
    }

    fun deleteVideoFavorite(idVideo: String): LiveData<ApiResult<CommonResponse>> {
        return learningUseCase.deleteVideoFavorite(idVideo).asLiveData()
    }

    fun saveLastSeenVideo(idVideo: String, linkVideo:String): LiveData<ApiResult<CommonResponse>> {
        return learningUseCase.saveLastSeenVideo(idVideo, linkVideo).asLiveData()
    }
}