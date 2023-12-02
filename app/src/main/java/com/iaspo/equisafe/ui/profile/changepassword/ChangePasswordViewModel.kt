package com.iaspo.equisafe.ui.profile.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iaspo.equisafe.core.data.network.request.ChangePasswordRequest
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.usecase.AccountUseCase

class ChangePasswordViewModel(private val accountUseCase: AccountUseCase): ViewModel() {
    fun changePassword(changePasswordRequest: ChangePasswordRequest): LiveData<ApiResult<CommonResponse>> {
        return accountUseCase.changePassword(changePasswordRequest).asLiveData()
    }
}