package com.iaspo.equisafe.core.data.network.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    val password: String,
    @SerializedName("new_password")
    val newPassword: String
)