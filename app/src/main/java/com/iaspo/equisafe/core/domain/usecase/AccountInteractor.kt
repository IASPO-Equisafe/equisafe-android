package com.iaspo.equisafe.core.domain.usecase

import com.iaspo.equisafe.core.data.network.request.ChangePasswordRequest
import com.iaspo.equisafe.core.data.network.request.SaveDataRequest
import com.iaspo.equisafe.core.data.network.response.accountresponse.AccountResponse
import com.iaspo.equisafe.core.data.network.response.CommonResponse
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.repository.IAccountRepository
import kotlinx.coroutines.flow.Flow

class AccountInteractor(private val accountRepository: IAccountRepository): AccountUseCase {

    override fun getAccountData(): Flow<ApiResult<AccountResponse>> {
        return accountRepository.getAccountData()
    }

    override fun logout(): Flow<ApiResult<CommonResponse>> {
        return accountRepository.logout()
    }

    override fun saveAccountData(saveDataRequest: SaveDataRequest): Flow<ApiResult<CommonResponse>> {
        return accountRepository.saveAccountData(saveDataRequest)
    }

    override fun changePassword(changePasswordRequest: ChangePasswordRequest): Flow<ApiResult<CommonResponse>> {
        return accountRepository.changePassword(changePasswordRequest)
    }
}