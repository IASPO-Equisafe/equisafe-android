package com.iaspo.equisafe.core.domain.repository

import com.iaspo.equisafe.core.data.network.request.ChangeEmergencyRequest
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import kotlinx.coroutines.flow.Flow

interface IEmergencyRepository {
    fun getEmergencyName(): Flow<String>
    fun getEmergencyNumber(): Flow<String>
    fun changeEmergency(changeEmergencyRequest: ChangeEmergencyRequest): Flow<ApiResult<CommonResponse>>
}