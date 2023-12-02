package com.iaspo.equisafe.core.data.repository

import android.util.Log
import com.google.gson.Gson
import com.iaspo.equisafe.core.constant.DataStoreConstant
import com.iaspo.equisafe.core.data.local.UserPreferences
import com.iaspo.equisafe.core.data.network.request.LoginRequest
import com.iaspo.equisafe.core.data.network.request.RegisterRequest
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.response.authresponse.LoginResponse
import com.iaspo.equisafe.core.data.network.response.authresponse.RegisterResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.data.network.retrofit.ApiService
import com.iaspo.equisafe.core.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.Exception

class AuthRepository(
    private val apiService: ApiService,
    private val dataStore: UserPreferences
): IAuthRepository {

    override fun loginUser(loginRequest: LoginRequest): Flow<ApiResult<LoginResponse>> = flow {
        emit(ApiResult.Loading())
        try {
            val response = apiService.userLogin(loginRequest)
            val responseBody = response.body()
            if (response.code() == 200 && responseBody != null) {
                emit(ApiResult.Success(responseBody))
                val save = dataStore.saveStringData(stringValue = DataStoreConstant.BEARER + responseBody.data.accessToken, key = DataStoreConstant.TOKEN)
                Log.e("Data User", responseBody.data.accessToken)
                Log.e("Save Data User", save.toString())
            } else if (responseBody.isNull && response.code() != 400){
                emit(ApiResult.Empty)
            } else {
                val gson = Gson()
                val errorResponse = response.errorBody()?.string()
                val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                emit(ApiResult.Error(errorMessage.message))
            }
        } catch (e: Exception){
            emit(ApiResult.Error(e.message ?: "Unknown Error"))
        }
    }

    override fun registerUser(registerRequest: RegisterRequest): Flow<ApiResult<RegisterResponse>> = flow {
        emit(ApiResult.Loading())
        try {
            val response = apiService.userRegister(registerRequest)
            val responseBody = response.body()
            if (response.code() == 200 && responseBody != null) {
                emit(ApiResult.Success(responseBody))
            } else if (responseBody.isNull && response.code() != 400){
                emit(ApiResult.Empty)
            } else {
                val gson = Gson()
                val errorResponse = response.errorBody()?.string()
                val errorMessage = gson.fromJson(errorResponse, CommonResponse::class.java)
                emit(ApiResult.Error(errorMessage.message))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Unknown Message"))
        }
    }
}

val Any?.isNull get() = this == null

