package com.healthit.application.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.healthit.application.R
import com.healthit.application.data.AppPreferences
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.repository.DataRepository
import com.healthit.application.ui.splash.LauncherActivity
import com.healthit.application.utils.constant.HelperConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var TAG = "MyFirebaseMessagingService"
    var notificationBuilder: Notification.Builder? = null
    var nmc: NotificationManagerCompat? = null

    @Inject
    lateinit var dataRepository: DataRepository

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "health_it_channel"
    }

    override fun onNewToken(fcmToken: String) {
        fcmToken.let {
            AppPreferences.fcmToken = it
            CoroutineScope(Dispatchers.IO).launch {
                dataRepository.updateUser()
            }
        }

        super.onNewToken(fcmToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "FROM : " + remoteMessage.from)

        //Verify if the message contains data
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data : " + remoteMessage.data)
        }

        //Verify if the message contains notification
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message body : " + remoteMessage.data.toString())
            sendNotification(remoteMessage)
        }
    }

    private fun sendNotification(body: RemoteMessage) {
        val title = body.data["title"]
        val message = body.data["message"]
        val data = Gson().fromJson(body.data["appointmentData"], AppointmentModel::class.java)

        val intent = Intent(this, LauncherActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL, data)


        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT/*Flag indicating that this PendingIntent can be used only once.*/)
        } else {
            PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                "HealthIt",
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "health it notification"

            val manager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            notificationBuilder = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            notificationBuilder?.apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle(title)
                setContentText(message)
                setAutoCancel(true)
                setContentIntent(pendingIntent)
                setPriority(Notification.PRIORITY_HIGH)
            }


            nmc = NotificationManagerCompat.from(this)
            nmc!!.notify(0, notificationBuilder!!.build())
        } else {
            notificationBuilder = Notification.Builder(this)
            notificationBuilder?.apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle(title)
                setContentText(message)
                setAutoCancel(true)
                setContentIntent(pendingIntent)
                setPriority(Notification.PRIORITY_HIGH)
            }


            nmc = NotificationManagerCompat.from(this)
            nmc!!.notify(0, notificationBuilder!!.build())
        }
    }
}