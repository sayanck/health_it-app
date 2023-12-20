package com.healthit.application.ui.doctor.dashboard

import android.os.Build
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.healthit.application.R
import com.healthit.application.base.activity.BaseActivity
import com.healthit.application.databinding.ActivityDoctorDashboardBinding
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.model.request.notificationRequuest.Data
import com.healthit.application.ui.doctor.patientDetail.PatientDetailActivity
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorDashboardActivity :
    BaseActivity<ActivityDoctorDashboardBinding, DoctorDashboardViewModel>(R.layout.activity_doctor_dashboard) {

    private lateinit var navController: NavController

    private val mViewModel: DoctorDashboardViewModel by viewModels()


    override fun getBindingVariable(): Int = R.layout.activity_doctor_dashboard

    override fun getViewModel(): DoctorDashboardViewModel = mViewModel


    override fun onInitDataBinding(viewBinding: ActivityDoctorDashboardBinding) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_doctor) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(viewBinding.doctorNavigationView, navController)
        handleNotification()
    }

    fun changeTab(id: Int) {
        getViewDataBinding().doctorNavigationView.selectedItemId = id
    }

    private fun handleNotification() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL,
                AppointmentModel::class.java)
        } else {
            intent.getParcelableExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL) as AppointmentModel?
        }
        data?.let {
            startActivity<PatientDetailActivity> {
                putExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL, it)
            }
        }

    }

}