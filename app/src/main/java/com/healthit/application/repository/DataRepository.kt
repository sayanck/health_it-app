package com.healthit.application.repository

import androidx.lifecycle.MediatorLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.healthit.application.data.AppPreferences
import com.healthit.application.data.network.api.HealthItApi
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.model.request.notificationRequuest.PushNotificationRequest
import com.healthit.application.model.response.notificaitonResponse.PushNotificationResponse
import com.healthit.application.model.response.userDetails.User
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.livedata.EventObject
import com.healthit.application.utils.livedata.LiveDataEvent
import com.healthit.application.utils.logD
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DataRepository @Inject constructor(
    private val healthItApi: HealthItApi,
) {
    private val tag = DataRepository::class.java.simpleName
    private var mBroadcastEvent: MediatorLiveData<LiveDataEvent<EventObject>> = MediatorLiveData()
    private var auth: FirebaseAuth = Firebase.auth
    private var db = Firebase.firestore

    companion object {
        val TAG = DataRepository::class.java.simpleName
    }


    fun getBroadCastEvent(): MediatorLiveData<LiveDataEvent<EventObject>> {
        return mBroadcastEvent
    }

    suspend fun sendNotification(notificationRequest: PushNotificationRequest): Response<PushNotificationResponse> {
       return healthItApi.sendNotification(notificationRequest)
    }

    suspend fun fetchUser(): User? {
        auth.currentUser?.let {
            val result = suspendCoroutine { continuation ->
                db.collection(HelperConstant.CollectionName.sUsers).document(it.uid).get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val userData = documentSnapshot.toObject(User::class.java)
                            userData?.let { user ->
                                continuation.resume(user)
                            }
                        } else {
                            continuation.resume(null)
                        }
                    }.addOnFailureListener { ex ->
                        logD("$TAG  ${ex.message.toString()}")
                        continuation.resume(null)
                    }
            }
            return result
        } ?: run {
            return null
        }
    }

    suspend fun fetchUserById(userId: String): User? {
        val result = suspendCoroutine { continuation ->
            db.collection(HelperConstant.CollectionName.sUsers).document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userData = documentSnapshot.toObject(User::class.java)
                        userData?.let { user ->
                            continuation.resume(user)
                        }
                    } else {
                        continuation.resume(null)
                    }
                }.addOnFailureListener { ex ->
                    logD("$TAG  ${ex.message.toString()}")
                    continuation.resume(null)
                }
        }
        return result
    }


    suspend fun updateUser(): Boolean {
        val result = suspendCoroutine { continuation ->
            auth.currentUser?.let {
                db.collection(HelperConstant.CollectionName.sUsers).document(it.uid)
                    .update(HelperConstant.ParamsKey.sACCESS_TOKEN, AppPreferences.fcmToken)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    }.addOnFailureListener { ex ->
                        logD("$TAG  ${ex.message.toString()}")
                        continuation.resume(false)
                    }
            }
        }
        return result
    }

    suspend fun requestAndUpdateAppointment(appointmentModel: AppointmentModel): Boolean {
        val result = suspendCoroutine { continuation ->
            db.collection(HelperConstant.CollectionName.sAppointments).document(appointmentModel.id)
                .set(appointmentModel)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                    }
                }.addOnFailureListener { ex ->
                    logD("$TAG  ${ex.message.toString()}")
                    continuation.resume(false)
                }
        }
        return result
    }

    suspend fun fetchPatientAllAppointments(): List<AppointmentModel?> {
        val result = suspendCoroutine<ArrayList<AppointmentModel?>> { continuation ->
            auth.currentUser?.let {
                db.collection(HelperConstant.CollectionName.sAppointments)
                    .whereEqualTo(HelperConstant.ParamsKey.sPATIENT_ID, it.uid)
                    .get()
                    .addOnSuccessListener { documents ->
                        val list = arrayListOf<AppointmentModel?>()
                        for (document in documents) {
                            val userData =
                                document.toObject(AppointmentModel::class.java) as AppointmentModel?
                            userData?.let { user ->
                                list.add(user)
                            }
                        }
                        continuation.resume(list)
                    }.addOnFailureListener { ex ->
                        logD("$TAG  ${ex.message.toString()}")
                        continuation.resume(arrayListOf())
                    }
            }
        }
        return result
    }

    suspend fun fetchDoctorAllAppointments(): List<AppointmentModel?> {
        val result = suspendCoroutine<ArrayList<AppointmentModel?>> { continuation ->
            auth.currentUser?.let {
                db.collection(HelperConstant.CollectionName.sAppointments)
                    .whereEqualTo(HelperConstant.ParamsKey.sDOCTOR_ID, it.uid).get()
                    .addOnSuccessListener { documents ->
                        val list = arrayListOf<AppointmentModel?>()
                        for (document in documents) {
                            val userData =
                                document.toObject(AppointmentModel::class.java) as AppointmentModel?
                            userData?.let { user ->
                                list.add(user)
                            }
                        }
                        continuation.resume(list)
                    }.addOnFailureListener { ex ->
                        logD("$TAG  ${ex.message.toString()}")
                        continuation.resume(arrayListOf())
                    }
            }
        }
        return result
    }

}
