package com.iaspo.equisafe.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val username: String? = null,
    val fullName: String? = null,
    val emergencyName: String? = null,
    val emergencyNumber: String? = null
): Parcelable