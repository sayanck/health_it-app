package com.healthit.application.ui.patient.doctorDetail

import android.os.Build
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.healthit.application.R
import com.healthit.application.base.activity.BaseActivity
import com.healthit.application.databinding.ActivityDoctorDetailsBinding
import com.healthit.application.model.response.userDetails.User
import com.healthit.application.utils.constant.HelperConstant
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DoctorDetailsActivity :
    BaseActivity<ActivityDoctorDetailsBinding, DoctorDetailsViewModel>(R.layout.activity_doctor_details) {

    private val mViewModel: DoctorDetailsViewModel by viewModels()

    private lateinit var navController: NavController

    override fun getBindingVariable(): Int = R.layout.activity_doctor_details

    override fun getViewModel(): DoctorDetailsViewModel = mViewModel

    override fun onInitDataBinding(viewBinding: ActivityDoctorDetailsBinding) {
        init()
    }

    private fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_booking_container) as NavHostFragment
        navController = navHostFragment.navController

        mViewModel.userLiveData.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(HelperConstant.IntentParams.sDOCTOR_DETAIL, User::class.java)
        } else {
            intent.getParcelableExtra(HelperConstant.IntentParams.sDOCTOR_DETAIL) as User?
        }
    }

}