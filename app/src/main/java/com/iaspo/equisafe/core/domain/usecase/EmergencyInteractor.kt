package com.iaspo.equisafe.core.domain.usecase

import com.iaspo.equisafe.core.data.network.request.ChangeEmergencyRequest
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.repository.IEmergencyRepository
import kotlinx.coroutines.flow.Flow

class EmergencyInteractor(private val emergencyRepository: IEmergencyRepository): EmergencyUseCase {
    override fun getEmergencyName(): Flow<String> {
        return emergencyRepository.getEmergencyName()
    }

    override fun getEmergencyNumber(): Flow<String> {
        return emergencyRepository.getEmergencyNumber()
    }

    override fun changeEmergency(changeEmergencyRequest: ChangeEmergencyRequest): Flow<ApiResult<CommonResponse>> {
        return emergencyRepository.changeEmergency(changeEmergencyRequest)
    }
}