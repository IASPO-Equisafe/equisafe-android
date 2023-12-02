package com.iaspo.equisafe.core.data.network.retrofit

sealed class ApiResult<out R> {
    data class Success<out T>(val data: T): ApiResult<T>()

    data class Loading<out T>(val data: T? = null): ApiResult<T>()

    data class Error(val errorMessage: String): ApiResult<Nothing>()

    object Empty: ApiResult<Nothing>()
}
