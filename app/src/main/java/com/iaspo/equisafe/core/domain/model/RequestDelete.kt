package com.iaspo.equisafe.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestDelete(
    @SerializedName("delete_all")
    val deleteAll: Boolean,

    @SerializedName("id_video")
    val idVideo: String
): Parcelable
