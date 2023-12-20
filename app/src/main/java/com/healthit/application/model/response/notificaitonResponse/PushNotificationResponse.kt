package com.healthit.application.model.response.notificaitonResponse

data class PushNotificationResponse(
    val canonical_ids: Int?,
    val failure: Int?,
    val multicast_id: Long?,
    val results: List<Result>?,
    val success: Int?
)