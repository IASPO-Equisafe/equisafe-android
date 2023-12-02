package com.iaspo.equisafe.core.data.repository

import com.google.gson.Gson
import com.iaspo.equisafe.core.constant.DataStoreConstant
import com.iaspo.equisafe.core.data.local.UserPreferences
import com.iaspo.equisafe.core.data.network.request.ChangeEmergencyRequest
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.data.network.retrofit.ApiService
import com.iaspo.equisafe.core.domain.repository.IEmergencyRepository
import com.iaspo.equisafe.core.utils.withNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class EmergencyRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreferences
): IEmergencyRepository {

    override fun getEmergencyName(): Flow<String> = flow {
        val emergencyName = withContext(Dispatchers.IO) {
            dataStore.getStringData(DataStoreConstant.EMERGENCY_NAME).firstOrNull()
        }

        emergencyName.withNotNull { name ->
            emit(name)
        }
    }

    override fun getEmergencyNumber(): Flow<String> = flow {
        val emergencyNumber = withContext(Dispatchers.IO) {
            dataStore.getStringData(DataStoreConstant.EMERGENCY_NUMBER).firstOrNull()
        }

        emergencyNumber.withNotNull { number ->
            emit(number)
        }
    }

    override fun changeEmergency(changeEmergencyRequest: ChangeEmergencyRequest): Flow<ApiResult<CommonResponse>> = flow {
        val userSession = withContext(Dispatchers.IO){
            dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
        }

        try {
            emit(ApiResult.Loading())
            userSession.withNotNull { token ->
                val response = apiService.changeEmergency(token = token, changeEmergencyRequest = changeEmergencyRequest)
                val responseBody = response.body()
                if (response.code() == 200 && responseBody != null) {
                    dataStore.saveStringData(changeEmergencyRequest.name, DataStoreConstant.EMERGENCY_NAME)
                    dataStore.saveStringData(changeEmergencyRequest.telp, DataStoreConstant.EMERGENCY_NUMBER)
                    emit(ApiResult.Success(responseBody))
                } else if (responseBody.isNull && response.code() != 400) {
                    emit(ApiResult.Empty)
                } else {
                    val gson = Gson()
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                    emit(ApiResult.Error(errorMessage.message))
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Message"))
        }
    }


}