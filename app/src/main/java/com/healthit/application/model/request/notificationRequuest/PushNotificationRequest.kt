package com.healthit.application.model.request.notificationRequuest

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PushNotificationRequest(
    val data: Data,
    val to: String? = "",
) : Parcelable