package com.iaspo.equisafe.core.domain.repository

import com.iaspo.equisafe.core.data.network.request.LoginRequest
import com.iaspo.equisafe.core.data.network.request.RegisterRequest
import com.iaspo.equisafe.core.data.network.response.authresponse.LoginResponse
import com.iaspo.equisafe.core.data.network.response.authresponse.RegisterResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun loginUser(loginRequest: LoginRequest): Flow<ApiResult<LoginResponse>>

    fun registerUser(registerRequest: RegisterRequest): Flow<ApiResult<RegisterResponse>>
}