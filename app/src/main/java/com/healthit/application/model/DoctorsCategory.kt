package com.healthit.application.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoctorsCategory(
    val name: String,
    val drawable: Int,
) : Parcelable
