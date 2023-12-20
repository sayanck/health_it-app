package com.healthit.application.ui.profile

import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.data.AppPreferences
import com.healthit.application.databinding.FragmentProfileBinding
import com.healthit.application.ui.loginsignup.LoginSignUpActivity
import com.healthit.application.ui.patient.dashboard.PatientMainViewModel
import com.healthit.application.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientProfileFragment :
    BaseFragment<FragmentProfileBinding, PatientMainViewModel>(R.layout.fragment_profile) {

    private val mViewModel: PatientMainViewModel by activityViewModels()

    override fun getViewModel(): PatientMainViewModel = mViewModel

    override fun onInitDataBinding(viewBinding: FragmentProfileBinding) {
        setClickListener(viewBinding)
        init(viewBinding)
    }

    private fun init(viewBinding: FragmentProfileBinding) {
        AppPreferences.userDetails?.let {
            viewBinding.user = it
            if (it.doctorSpeciality.isEmpty()) {
                viewBinding.specialityGroup.visibility = View.GONE
            } else {
                viewBinding.specialityGroup.visibility = View.VISIBLE
            }
        }
    }

    private fun setClickListener(viewBinding: FragmentProfileBinding) {
        viewBinding.logoutBt.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            activity?.startActivity<LoginSignUpActivity>()
            activity?.finish()
        }
    }


}