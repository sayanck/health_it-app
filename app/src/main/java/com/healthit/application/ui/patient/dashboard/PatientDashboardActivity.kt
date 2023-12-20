package com.healthit.application.ui.patient.dashboard

import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.healthit.application.R
import com.healthit.application.base.activity.BaseActivity
import com.healthit.application.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientDashboardActivity :
    BaseActivity<ActivityMainBinding, PatientMainViewModel>(R.layout.activity_main) {

    private lateinit var navController: NavController

    private val mViewModel: PatientMainViewModel by viewModels()


    override fun getBindingVariable(): Int = R.layout.activity_main

    override fun getViewModel(): PatientMainViewModel = mViewModel


    override fun onInitDataBinding(viewBinding: ActivityMainBinding) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_patient) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(viewBinding.patientNavigationView, navController)
    }


}