package com.healthit.application.ui.loginsignup.doctorPatientChooser

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.data.AppPreferences
import com.healthit.application.databinding.FragmentDoctorPatientChooserBinding
import com.healthit.application.ui.loginsignup.LoginRegisterViewModel
import com.healthit.application.utils.constant.HelperConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorPatientChooserFragment :
    BaseFragment<FragmentDoctorPatientChooserBinding, LoginRegisterViewModel>(
        R.layout.fragment_doctor_patient_chooser
    ) {

    companion object {
        const val TAG = "Login"
    }

    private val mViewModel: LoginRegisterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback {
            activity?.finish()
        }
    }

    override fun onInitDataBinding(viewBinding: FragmentDoctorPatientChooserBinding) {
        viewBinding.apply {
            setClickListener(this)
        }

    }

    private fun setClickListener(binding: FragmentDoctorPatientChooserBinding) {
        binding.patientLoginBt.setOnClickListener {
            AppPreferences.userType = HelperConstant.UserType.PATIENT.name
            findNavController().navigate(R.id.loginFragment)
        }

        binding.doctorLoginBt.setOnClickListener {
            AppPreferences.userType = HelperConstant.UserType.DOCTOR.name
            findNavController().navigate(R.id.loginFragment)
        }
    }


    override fun getViewModel(): LoginRegisterViewModel = mViewModel
}