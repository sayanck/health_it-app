package com.healthit.application.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppointmentModel(
    val id: String = "",
    val doctorId: String? = "",
    val doctorName: String? = "",
    val patientName: String? = "",
    val patientId: String? = "",
    val problemDescription: String? = "",
    val startTime: String? = "",
    var status: String = "",
    var paid: Boolean = false,
) : Parcelable
