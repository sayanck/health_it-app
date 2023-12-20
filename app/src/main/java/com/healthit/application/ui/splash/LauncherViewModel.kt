package com.healthit.application.ui.splash

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.healthit.application.base.viewmodel.BaseViewModel
import com.healthit.application.model.response.userDetails.User
import com.healthit.application.repository.DataRepository
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.logD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class LauncherViewModel @Inject constructor(
    application: Application,
    dataRepository: DataRepository,
) : BaseViewModel(application, dataRepository) {

    companion object {
        const val TAG = "LoginRegisterViewModel:"
    }

    private var auth = Firebase.auth
    private var db = Firebase.firestore
    var userResponse = MutableLiveData<User?>()
    private val handler = CoroutineExceptionHandler { _, exception ->
        logD("$TAG, exception  :  $exception")
    }

    init {
        fetchUser()
    }


    private fun fetchUser() {
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
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
                        }
                        .addOnFailureListener { ex ->
                            showMessage("Fetch User Error :  $ex")
                            continuation.resume(null)
                        }
                }
                delay(2000)
                userResponse.postValue(result)
            } ?: run {
                delay(2000)
                userResponse.postValue(null)
            }

        }

        job.invokeOnCompletion {
            if (it != null) {
                userResponse.postValue(null)
                showMessage(it.message.toString())
                logD("$TAG job Fetch User :$it")
            }
        }
    }

}