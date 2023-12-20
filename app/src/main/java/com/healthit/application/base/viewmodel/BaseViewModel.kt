package com.healthit.application.base.viewmodel

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.healthit.application.data.AppPreferences
import com.healthit.application.repository.DataRepository
import com.healthit.application.utils.livedata.EventObject
import com.healthit.application.utils.livedata.LiveDataEvent
import com.healthit.application.utils.logD
import java.util.*

open class BaseViewModel(application: Application, repository: DataRepository) :
    AndroidViewModel(application) {

    var errorLiveData: MutableLiveData<LiveDataEvent<String>> = MutableLiveData()
    var loadingLiveData: MutableLiveData<LiveDataEvent<Boolean>> = MutableLiveData()
    var messageLiveData: MutableLiveData<LiveDataEvent<String>> = MutableLiveData()
    var messageInDialogLiveData: MutableLiveData<LiveDataEvent<String>> = MutableLiveData()
    private var mBroadcastEvent: LiveData<LiveDataEvent<EventObject>>? = null
    val loginErrorLiveData = MutableLiveData<String>()

    init {
        this.mBroadcastEvent = repository.getBroadCastEvent()

        /*  val config = application.resources.configuration
          Locale.setDefault(Locale(AppPreferences.selectedLanguage))
          config.locale = Locale(AppPreferences.selectedLanguage)
          application.resources.updateConfiguration(config, application.resources.displayMetrics)*/
    }

    fun onError(error: String) {
        errorLiveData.postValue(LiveDataEvent(error))
    }

    fun showMessage(message: String) {
        messageLiveData.postValue(LiveDataEvent(message))
    }

    fun showMessageInDialog(message: String) {
        messageInDialogLiveData.postValue(LiveDataEvent(message))
    }

    fun showLoading(visible: Boolean) {
        loadingLiveData.postValue(LiveDataEvent(visible))
    }

    fun String.showErrorMessage(resString: Resources) {
        logD("error message is : $this")
        showMessage(this)
    }
}