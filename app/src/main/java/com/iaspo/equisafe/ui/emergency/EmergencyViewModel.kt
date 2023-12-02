package com.iaspo.equisafe.ui.emergency

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iaspo.equisafe.core.data.network.request.ChangeEmergencyRequest
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.usecase.EmergencyUseCase

class EmergencyViewModel(private val emergencyUseCase: EmergencyUseCase): ViewModel() {
    fun getEmergencyName(): LiveData<String> {
        return emergencyUseCase.getEmergencyName().asLiveData()
    }

    fun getEmergencyNumber(): LiveData<String> {
        return emergencyUseCase.getEmergencyNumber().asLiveData()
    }

    fun changeEmergency(changeEmergencyRequest: ChangeEmergencyRequest): LiveData<ApiResult<CommonResponse>> {
        return emergencyUseCase.changeEmergency(changeEmergencyRequest).asLiveData()
    }
}