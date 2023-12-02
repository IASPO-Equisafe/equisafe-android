package com.iaspo.equisafe.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LearningModel(
    val id: String,
    val title: String,
    val description: String,
    val link: String,
    val thumbnailLink: String,
    val isFavorite: Boolean
): Parcelable