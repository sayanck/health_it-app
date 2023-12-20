package com.healthit.application.ui.patient.dashboard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.healthit.application.base.viewmodel.BaseViewModel
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.repository.DataRepository
import com.healthit.application.ui.doctor.dashboard.DoctorDashboardViewModel
import com.healthit.application.ui.loginsignup.LoginRegisterViewModel
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.logD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientMainViewModel @Inject constructor(
    application: Application,
    private val dataRepository: DataRepository,
) : BaseViewModel(application, dataRepository) {

    companion object {
        const val TAG = "PatientMainViewModel:"
    }

    private val _appointmentResponse = MutableLiveData<List<AppointmentModel?>>()
    val appointmentResponse get() = _appointmentResponse

    private val _updateAppointmentResponse = MutableLiveData(false)
    val updateAppointmentResponse: LiveData<Boolean>
        get() = _updateAppointmentResponse

    private val handler = CoroutineExceptionHandler { _, exception ->
        logD("${LoginRegisterViewModel.TAG}, exception  :  $exception")
    }

    fun fetchAllAppointments() {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            delay(1000)
            val result = dataRepository.fetchPatientAllAppointments()
            _appointmentResponse.postValue(result)
            showLoading(false)
        }

        job.invokeOnCompletion {
            if (it != null) {
                showLoading(false)
                showMessage(it.message.toString())
                _appointmentResponse.postValue(arrayListOf())
                logD("$TAG job fetch Appointments :$it")
            }
        }
    }

    fun fetchDoctorAppointments() {
        showLoading(true)
        val job = viewModelScope.launch(Dispatchers.IO + handler) {
            val result = dataRepository.fetchDoctorAllAppointments()
            _appointmentResponse.postValue(result)
            showLoading(false)
        }

        job.invokeOnCompletion {
            if (it != null) {
                showLoading(false)
                showMessage(it.message.toString())
                _appointmentResponse.postValue(arrayListOf())
                logD("${DoctorDashboardViewModel.TAG} job fetch Doctors Appointments :$it")
            }
        }
    }

    fun markAsPaid(appointmentData: AppointmentModel) {
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