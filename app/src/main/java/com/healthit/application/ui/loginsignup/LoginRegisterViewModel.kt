package com.healthit.application.ui.loginsignup

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.healthit.application.base.viewmodel.BaseViewModel
import com.healthit.application.data.AppPreferences
import com.healthit.application.model.response.userDetails.User
import com.healthit.application.repository.DataRepository
import com.healthit.application.ui.splash.LauncherViewModel
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.logD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    application: Application,
    val dataRepository: DataRepository,
) : BaseViewModel(application, dataRepository) {

    companion object {
        const val TAG = "LoginRegisterViewModel:"
    }

    private var auth: FirebaseAuth = Firebase.auth
    private var db = Firebase.firestore

    var nameLiveData = MutableLiveData<String>()
    val emailLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val specialityLiveData = MutableLiveData<String>()
    val userResponse = MutableLiveData<User?>()

    private val handler = CoroutineExceptionHandler { _, exception ->
        logD("$TAG, exception  :  $exception")
    }

    fun login(email: String, password: String) {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    fetchUser(fromLogin = true)
                } else {
                    showMessage("Invalid Email and Password")
                }
                showLoading(false)
            }
        }

        job.invokeOnCompletion {
            if (it != null) {
                showLoading(false)
                showMessage(it.message.toString())
                logD("$TAG jobSignup :$it")
            }
        }
    }

    fun signUp() {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            auth.createUserWithEmailAndPassword(emailLiveData.value ?: "",
                passwordLiveData.value ?: "").addOnCompleteListener {
                if (it.isSuccessful) {
                    showLoading(false)
                    addUser()
                } else {
                    showMessage("Signup Error :  ${it.exception}")
                }
            }
        }

        job.invokeOnCompletion {
            if (it != null) {
                showLoading(false)
                showMessage(it.message.toString())
                logD("$TAG jobSignup :$it")
            }
        }
    }

    /* private fun updateProfile(name: String) {
         showLoading(true)
         val job = viewModelScope.launch(Dispatchers.IO + handler) {
             val user = Firebase.auth.currentUser

             val profileUpdates = userProfileChangeRequest {
                 displayName = name
             }

             user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     showLoading(false)
                     mLoggedIn.postValue(auth.currentUser)
                 } else {
                     showMessage("Signup Error :  ${task.exception}")
                 }
             }
         }
         job.invokeOnCompletion {
             if (it != null) {
                 showLoading(false)
                 showMessage(it.message.toString())
                 logD("$TAG jobSignup :$it")
             }
         }
     }*/

    private fun addUser() {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            auth.currentUser?.let {
                val user = User(
                    id = it.uid,
                    name = nameLiveData.value ?: "",
                    email = it.email ?: "",
                    type = AppPreferences.userType,
                    doctorSpeciality = specialityLiveData.value ?: "",
                    accessToken = AppPreferences.fcmToken)
                db.collection(HelperConstant.CollectionName.sUsers).document(it.uid).set(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showLoading(false)
                            fetchUser()
                        } else {
                            showMessage("Add User Error :  ${task.exception}")
                        }
                    }.addOnFailureListener { ex ->
                        showMessage("Add User Error :  $ex")
                    }
            }

        }

        job.invokeOnCompletion {
            if (it != null) {
                showLoading(false)
                showMessage(it.message.toString())
                logD("$TAG jobSignup :$it")
            }
        }
    }

    private fun fetchUser(fromLogin: Boolean = false) {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            val response = dataRepository.fetchUser()
            response?.let {
                if (fromLogin) {
                    updateUser(user = response)
                } else {
                    userResponse.postValue(it)
                }
            } ?: run {
                userResponse.postValue(null)
                showMessage("User is null")
            }
            showLoading(false)

        }

        job.invokeOnCompletion {
            if (it != null) {
                showLoading(false)
                showMessage(it.message.toString())
                logD("${LauncherViewModel::class.java} job fetch User :$it")
            }
        }
    }

    private fun updateUser(user: User?) {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            dataRepository.updateUser()
            userResponse.postValue(user)
        }

        job.invokeOnCompletion {
            if (it != null) {
                showLoading(false)
                showMessage(it.message.toString())
                logD("${LauncherViewModel::class.java} job Update User :$it")
            }
        }
    }
}