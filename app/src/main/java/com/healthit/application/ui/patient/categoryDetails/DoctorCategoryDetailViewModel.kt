package com.healthit.application.ui.patient.categoryDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.healthit.application.base.viewmodel.BaseViewModel
import com.healthit.application.model.response.userDetails.User
import com.healthit.application.repository.DataRepository
import com.healthit.application.ui.loginsignup.LoginRegisterViewModel
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.logD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorCategoryDetailViewModel @Inject constructor(
    application: Application,
    dataRepository: DataRepository,
) : BaseViewModel(application, dataRepository) {

    companion object {
        const val TAG = "DoctorCategoryDetailViewModel:"
    }

    private var db = Firebase.firestore
    val userResponse = MutableLiveData<List<User?>>()


    private val handler = CoroutineExceptionHandler { _, exception ->
        logD("${LoginRegisterViewModel.TAG}, exception  :  $exception")
    }


    fun fetchDoctor(speciality: String) {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            db.collection(HelperConstant.CollectionName.sUsers)
                .whereEqualTo("doctorSpeciality", speciality).get()
                .addOnSuccessListener { documents ->
                    val list = arrayListOf<User?>()
                    for (document in documents) {
                        val userData = document.toObject(User::class.java) as User?
                        userData?.let { user ->
                            list.add(user)
                        }
                    }
                    userResponse.postValue(list)
                    showLoading(false)
                }
                .addOnFailureListener { ex ->
                    showLoading(false)
                    userResponse.postValue(arrayListOf())
                    showMessage("Fetch Doctors Error :  $ex")
                }

        }

        job.invokeOnCompletion {
            if (it != null) {
                showLoading(false)
                showMessage(it.message.toString())
                userResponse.postValue(arrayListOf())
                logD("$TAG job fetch Doctors :$it")
            }
        }
    }
}