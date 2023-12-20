package com.healthit.application.ui.doctor.patientDetail


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.healthit.application.base.viewmodel.BaseViewModel
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.model.response.userDetails.User
import com.healthit.application.repository.DataRepository
import com.healthit.application.ui.doctor.dashboard.DoctorDashboardViewModel
import com.healthit.application.ui.loginsignup.LoginRegisterViewModel
import com.healthit.application.ui.splash.LauncherViewModel
import com.healthit.application.utils.logD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientDetailViewModel @Inject constructor(
    application: Application,
    private val dataRepository: DataRepository,
) : BaseViewModel(application, dataRepository) {

    companion object {
        const val TAG = "DoctorDashboardViewModel:"
    }

    private val _userResponse = MutableLiveData<User?>()
    val userResponse get() = _userResponse

    private val _updateAppointmentResponse = MutableLiveData(false)
    val updateAppointmentResponse: LiveData<Boolean>
        get() = _updateAppointmentResponse

    private val handler = CoroutineExceptionHandler { _, exception ->
        logD("${LoginRegisterViewModel.TAG}, exception  :  $exception")
    }


    fun fetchUser(userId: String) {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            val response = dataRepository.fetchUserById(userId)
            response?.let {
                userResponse.postValue(it)
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

    fun confirmAndDecline(appointmentData: AppointmentModel) {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            val result = dataRepository.requestAndUpdateAppointment(appointmentData)
            showLoading(false)
            _updateAppointmentResponse.postValue(result)
        }

        job.invokeOnCompletion {
            if (it != null) {
                showLoading(false)
                showMessage(it.message.toString())
                logD("${DoctorDashboardViewModel.TAG} job fetch Doctors Appointments :$it")
            }
        }
    }
}