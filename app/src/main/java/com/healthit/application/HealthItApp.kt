package com.healthit.application

import android.app.Application
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.healthit.application.data.AppPreferences
import com.healthit.application.utils.logD
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class HealthItApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initPreferences()
        FirebaseApp.initializeApp(this)
        getFcmToken()
    }

    private fun initPreferences() {
        AppPreferences.init(this)
    }


    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            AppPreferences.fcmToken = token
            logD("${HealthItApp::class.simpleName} Fcm Token :$token")
        })
    }

}