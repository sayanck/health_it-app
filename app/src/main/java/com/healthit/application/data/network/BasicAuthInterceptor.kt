package com.healthit.application.data.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor constructor(user: String, password: String) : Interceptor {

    private val credentials = Credentials.basic(user, password)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("User-Agent", "GrillHouse")
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", credentials).build()

        return chain.proceed(authenticatedRequest)
    }
}