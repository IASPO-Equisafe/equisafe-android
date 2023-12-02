package com.iaspo.equisafe.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iaspo.equisafe.core.data.network.response.accountresponse.AccountResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.usecase.AccountUseCase

class ProfileViewModel(private val accountUseCase: AccountUseCase): ViewModel() {
    val logout = accountUseCase.logout().asLiveData()

    fun getAccountData(): LiveData<ApiResult<AccountResponse>> {
        return accountUseCase.getAccountData().asLiveData()
    }
}