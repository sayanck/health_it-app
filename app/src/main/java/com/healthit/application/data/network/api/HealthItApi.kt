package com.healthit.application.data.network.api

import com.healthit.application.BuildConfig
import com.healthit.application.model.request.notificationRequuest.PushNotificationRequest
import com.healthit.application.model.response.loginRegister.LoginResponse
import com.healthit.application.model.response.loginRegister.SignupResponse
import com.healthit.application.model.response.notificaitonResponse.PushNotificationResponse
import com.healthit.application.utils.constant.ApiConstant
import com.healthit.application.utils.constant.ApiEndPoints
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface HealthItApi {
    //    @POST(ApiConstant.END_URL)
    suspend fun login(@Body request: RequestBody): Response<LoginResponse>

    //    @POST(ApiConstant.END_URL)
    suspend fun signUp(@Body mSignUpRequest: RequestBody): Response<SignupResponse>

    @Headers("Authorization: key=${BuildConfig.FIREBASE_KEY}", "Content-Type: application/json")
    @POST(ApiEndPoints.PUSH_NOTIFICATION)
    suspend fun sendNotification(@Body request: PushNotificationRequest): Response<PushNotificationResponse>

}