package com.iaspo.equisafe.core.data.repository

import com.google.gson.Gson
import com.iaspo.equisafe.core.constant.DataStoreConstant
import com.iaspo.equisafe.core.data.local.UserPreferences
import com.iaspo.equisafe.core.data.network.request.ChangePasswordRequest
import com.iaspo.equisafe.core.data.network.request.SaveDataRequest
import com.iaspo.equisafe.core.data.network.response.accountresponse.AccountResponse
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.data.network.retrofit.ApiService
import com.iaspo.equisafe.core.domain.repository.IAccountRepository
import com.iaspo.equisafe.core.utils.withNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.lang.Exception

class AccountRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreferences
) : IAccountRepository {

    override fun getAccountData(): Flow<ApiResult<AccountResponse>> = flow {
        val tokenUser = withContext(Dispatchers.IO) {
            dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
        }

        val usernameUser = withContext(Dispatchers.IO) {
            dataStore.getStringData(DataStoreConstant.USERNAME).firstOrNull()
        }

        try {
            emit(ApiResult.Loading())
            val response = apiService.getAccountData(token = tokenUser!!, username = usernameUser!!)
            val responseBody = response.body()

            if (responseBody != null) {
                dataStore.saveStringData(responseBody.data?.name.toString(), DataStoreConstant.FULL_NAME)
                emit(ApiResult.Success(responseBody))
            }  else {
                val gson = Gson()
                val errorResponse = response.errorBody()?.string()
                val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                emit(ApiResult.Error(errorMessage.message))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    override fun logout(): Flow<ApiResult<CommonResponse>> = flow {
        val tokenUser = withContext(Dispatchers.IO) {
            dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
        }

        tokenUser.withNotNull {
            try {
                emit(ApiResult.Loading())
                val response = apiService.logout(token = it)
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(ApiResult.Success(responseBody))
                    dataStore.clearUser()
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

    override fun saveAccountData(saveDataRequest: SaveDataRequest): Flow<ApiResult<CommonResponse>> =
        flow {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
            }

            tokenUser.withNotNull {
                try {
                    emit(ApiResult.Loading())
                    val response =
                        apiService.saveAccountData(token = it, saveDataRequest = saveDataRequest)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        emit(ApiResult.Success(responseBody))
                        dataStore.saveStringData(
                            stringValue = saveDataRequest.username,
                            key = DataStoreConstant.USERNAME
                        )
                        dataStore.saveStringData(
                            stringValue = saveDataRequest.name,
                            key = DataStoreConstant.FULL_NAME
                        )
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

    override fun changePassword(changePasswordRequest: ChangePasswordRequest): Flow<ApiResult<CommonResponse>> =
        flow {
            val tokenUser = withContext(Dispatchers.IO) {
                dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
            }

            tokenUser.withNotNull {
                try {
                    emit(ApiResult.Loading())
                    val response =
                        apiService.changePassword(
                            token = it,
                            changePasswordRequest = changePasswordRequest
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
                    emit(ApiResult.Error(e.message ?: "Unknown Error"))
                }
            }
        }
}