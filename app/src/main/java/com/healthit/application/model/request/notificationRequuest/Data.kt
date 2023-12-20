package com.healthit.application.model.request.notificationRequuest

import android.os.Parcelable
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.utils.constant.HelperConstant
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val message: String? = "",
    val title: String? = "",
    val type: String? = HelperConstant.NotificationType.APPOINTMENT.name,
    val appointmentData: AppointmentModel?,
) : Parcelable