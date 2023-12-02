package com.iaspo.equisafe.core.domain.usecase

import com.iaspo.equisafe.core.data.network.request.ChangeEmergencyRequest
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import kotlinx.coroutines.flow.Flow

interface EmergencyUseCase {
    fun getEmergencyName(): Flow<String>
    fun getEmergencyNumber(): Flow<String>
    fun changeEmergency(changeEmergencyRequest: ChangeEmergencyRequest): Flow<ApiResult<CommonResponse>>
}