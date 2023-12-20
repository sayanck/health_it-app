package com.healthit.application.ui.patient.doctorDetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.healthit.application.base.viewmodel.BaseViewModel
import com.healthit.application.data.AppPreferences
import com.healthit.application.model.CalenderModel
import com.healthit.application.model.Slot
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.model.request.notificationRequuest.Data
import com.healthit.application.model.request.notificationRequuest.PushNotificationRequest
import com.healthit.application.model.response.notificaitonResponse.PushNotificationResponse
import com.healthit.application.repository.DataRepository
import com.healthit.application.ui.loginsignup.LoginRegisterViewModel
import com.healthit.application.ui.splash.LauncherViewModel
import com.healthit.application.utils.HealthItUtils
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.logD
import dagger.hilt.android.lifecycle.HiltViewModel
import io.grpc.InternalChannelz.id
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DoctorDetailsViewModel @Inject constructor(
    application: Application,
    val dataRepository: DataRepository,
) : BaseViewModel(application, dataRepository) {
    companion object {
        const val TAG = "LoginRegisterViewModel:"
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        logD("${LoginRegisterViewModel.TAG}, exception  :  $exception")
    }

    val userLiveData = MutableLiveData<com.healthit.application.model.response.userDetails.User?>()
    private val _bookAppointmentResponse = MutableLiveData(false)
    val bookAppointmentResponse: LiveData<Boolean>
        get() = _bookAppointmentResponse

    //selected Date
    private val _selectedDateLiveData = MutableLiveData<CalenderModel?>()
    val selectedDateLiveData: LiveData<CalenderModel?>
        get() = _selectedDateLiveData

    // current date
    private val _currentDateLiveData = MutableLiveData<CalenderModel?>()
    val currentDateLiveData: LiveData<CalenderModel?>
        get() = _currentDateLiveData

    // notification response
    private val _notificationResponse = MutableLiveData<PushNotificationResponse?>()
    val notificationResponse: LiveData<PushNotificationResponse?>
        get() = _notificationResponse


    private val timeSlot = MutableLiveData<Slot?>()
    val problemFieldLiveData = MutableLiveData("")

    val slot: LiveData<Slot?>
        get() = timeSlot

    fun setTimeSlot(time: Slot?) {
        timeSlot.postValue(time)
    }

    fun setSelectedDate(date: CalenderModel) {
        _selectedDateLiveData.value = date
    }

    fun setCurrentDate(date: CalenderModel?) {
        _currentDateLiveData.value = date
    }

    fun bookAppointment() {
        showLoading(true)
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        selectedDateLiveData.value?.apply {
            calendar.time = data
            val date =
                "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH).plus(1)}/${
                    calendar.get(Calendar.YEAR)
                }"
            val appointmentData = AppointmentModel(
                id = HealthItUtils.generateUniqueId,
                patientId = Firebase.auth.currentUser?.uid,
                patientName = AppPreferences.userDetails?.name,
                doctorId = userLiveData.value?.id,
                doctorName = userLiveData.value?.name,
                problemDescription = problemFieldLiveData.value,
                startTime = "$date ${timeSlot.value?.timeString}",
                status = HelperConstant.AppointmentStatus.PENDING.name)

            val job = viewModelScope.launch(Dispatchers.IO + handler) {
                val result = dataRepository.requestAndUpdateAppointment(appointmentData)
                if (result) {
                    val notificationResponse = sendNotification(appointmentData)
                    if (notificationResponse.body() != null) {
                        _notificationResponse.postValue(notificationResponse.body())
                    } else {
                        showMessage(notificationResponse.errorBody().toString())
                    }
                }
                showLoading(false)
                _bookAppointmentResponse.postValue(result)
            }

            job.invokeOnCompletion {
                if (it != null) {
                    showLoading(false)
                    showMessage(it.message.toString())
                    logD("${LauncherViewModel::class.java} job fetch Book Appointment :$it")
                }
            }
        }
    }

    private suspend fun sendNotification(appointmentModel: AppointmentModel): Response<PushNotificationResponse> {
        val notificationRequest = PushNotificationRequest(
            to = userLiveData.value?.accessToken,
            data = Data(
                title = "Appointment Request",
                message = "${AppPreferences.userDetails?.name} sent you appointment request",
                type = HelperConstant.NotificationType.APPOINTMENT.name,
                appointmentData = appointmentModel
            )
        )
        return dataRepository.sendNotification(notificationRequest)
    }
}