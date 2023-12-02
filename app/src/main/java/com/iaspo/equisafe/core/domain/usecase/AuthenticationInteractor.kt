package com.iaspo.equisafe.core.domain.usecase

import com.iaspo.equisafe.core.data.network.request.LoginRequest
import com.iaspo.equisafe.core.data.network.request.RegisterRequest
import com.iaspo.equisafe.core.data.network.response.authresponse.LoginResponse
import com.iaspo.equisafe.core.data.network.response.authresponse.RegisterResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow

class AuthenticationInteractor(private val authRepository: IAuthRepository): AuthenticationUseCase {
    override fun registerUser(registerRequest: RegisterRequest): Flow<ApiResult<RegisterResponse>> {
        return authRepository.registerUser(registerRequest)
    }

    override fun loginUser(loginRequest: LoginRequest): Flow<ApiResult<LoginResponse>> {
        return authRepository.loginUser(loginRequest)
    }
}