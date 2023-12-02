package com.iaspo.equisafe.core.domain.repository

import com.iaspo.equisafe.core.data.network.request.ChangePasswordRequest
import com.iaspo.equisafe.core.data.network.request.SaveDataRequest
import com.iaspo.equisafe.core.data.network.response.accountresponse.AccountResponse
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import kotlinx.coroutines.flow.Flow

interface IAccountRepository {
    fun getAccountData(): Flow<ApiResult<AccountResponse>>

    fun logout(): Flow<ApiResult<CommonResponse>>

    fun saveAccountData(saveDataRequest: SaveDataRequest): Flow<ApiResult<CommonResponse>>

    fun changePassword(changePasswordRequest: ChangePasswordRequest): Flow<ApiResult<CommonResponse>>
}