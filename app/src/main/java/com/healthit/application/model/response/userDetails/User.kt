package com.healthit.application.model.response.userDetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var type: String = "",
    var doctorSpeciality: String = "",
    var accessToken: String = "",
) : Parcelable