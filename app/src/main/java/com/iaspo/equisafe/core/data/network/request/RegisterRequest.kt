package com.iaspo.equisafe.core.data.network.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRequest(
    val username: String,
    val password: String,
    val name: String,
    @SerializedName("emergency_name")
    val emergencyName: String,
    @SerializedName("emergency_telp")
    val emergencyTelp: String
): Parcelable