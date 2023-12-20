package com.healthit.application.ui.splash

import android.os.Build
import androidx.activity.viewModels
import com.healthit.application.R
import com.healthit.application.base.activity.BaseActivity
import com.healthit.application.databinding.ActivityLauncherBinding
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.model.request.notificationRequuest.Data
import com.healthit.application.ui.patient.dashboard.PatientDashboardActivity
import com.healthit.application.ui.doctor.dashboard.DoctorDashboardActivity
import com.healthit.application.ui.loginsignup.LoginSignUpActivity
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity :
    BaseActivity<ActivityLauncherBinding, LauncherViewModel>(R.layout.activity_launcher) {

    private val mViewModel: LauncherViewModel by viewModels()
    private var appointmentModel: AppointmentModel? = null

    override fun getBindingVariable(): Int = R.layout.activity_launcher

    override fun onInitDataBinding(viewBinding: ActivityLauncherBinding) {
        handleNotification()
        addObserver()
    }

    private fun handleNotification() {
        appointmentModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL,
                AppointmentModel::class.java)
        } else {
            intent.getParcelableExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL) as AppointmentModel?
        }
    }

    private fun addObserver() {
        mViewModel.userResponse.observe(this) {
            it?.let {
                if (it.type == HelperConstant.UserType.PATIENT.name) {
                    startActivity<PatientDashboardActivity>()
                } else {
                    startActivity<DoctorDashboardActivity> {
                        appointmentModel?.let { data ->
                            putExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL, data)
                        }
                    }
                }
            } ?: run {
                startActivity<LoginSignUpActivity>()
            }
            finish()
        }
    }

    override fun getViewModel(): LauncherViewModel = mViewModel

}