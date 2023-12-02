package com.iaspo.equisafe.core.data.network.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginRequest(
    val username: String,
    val password: String
): Parcelable