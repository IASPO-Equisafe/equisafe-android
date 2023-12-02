package com.iaspo.equisafe.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iaspo.equisafe.core.data.network.request.LoginRequest
import com.iaspo.equisafe.core.data.network.response.authresponse.LoginResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.usecase.AuthenticationUseCase

class LoginViewModel(private val authenticationUseCase: AuthenticationUseCase): ViewModel() {

    fun loginUser(loginRequest: LoginRequest): LiveData<ApiResult<LoginResponse>> {
        return authenticationUseCase.loginUser(loginRequest).asLiveData()
    }
}