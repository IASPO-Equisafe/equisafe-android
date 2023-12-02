package com.iaspo.equisafe.ui.profile.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iaspo.equisafe.core.data.network.request.SaveDataRequest
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.usecase.AccountUseCase

class EditProfileViewModel(private val accountUseCase: AccountUseCase): ViewModel() {

    fun saveAccountData(saveDataRequest: SaveDataRequest): LiveData<ApiResult<CommonResponse>> {
        return accountUseCase.saveAccountData(saveDataRequest).asLiveData()
    }
}