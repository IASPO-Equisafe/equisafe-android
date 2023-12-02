package com.iaspo.equisafe.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionGame(
    val question: String,
    val answer: String
): Parcelable
