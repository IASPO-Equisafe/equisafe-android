package com.iaspo.equisafe.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iaspo.equisafe.core.data.network.request.RegisterRequest
import com.iaspo.equisafe.core.data.network.response.authresponse.RegisterResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.usecase.AuthenticationUseCase

class RegisterViewModel(private val authenticationUseCase: AuthenticationUseCase): ViewModel() {
    fun registerUser(registerRequest: RegisterRequest): LiveData<ApiResult<RegisterResponse>> {
        return authenticationUseCase.registerUser(registerRequest).asLiveData()
    }
}