package com.healthit.application.ui.loginsignup.signup

import android.os.Bundle
import android.text.SpannableString
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.data.AppPreferences
import com.healthit.application.databinding.FragmentPatientSignupBinding
import com.healthit.application.ui.doctor.dashboard.DoctorDashboardActivity
import com.healthit.application.ui.loginsignup.LoginRegisterViewModel
import com.healthit.application.ui.patient.dashboard.PatientDashboardActivity
import com.healthit.application.utils.AppValidator
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.showToast
import com.healthit.application.utils.startActivity
import com.healthit.application.utils.underline
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment :
    BaseFragment<FragmentPatientSignupBinding, LoginRegisterViewModel>(R.layout.fragment_patient_signup) {

    companion object {
        const val TAG = "Login"
    }

    private val mViewModel: LoginRegisterViewModel by activityViewModels()
    private var accessToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onInitDataBinding(viewBinding: FragmentPatientSignupBinding) {
        viewBinding.apply {
            init(this)
            setClickListener(this)
            addObserver()
            if (AppPreferences.userType == HelperConstant.UserType.DOCTOR.name) {
                specialitySpinner.visibility = View.VISIBLE
                setDoctorSpecialitySpinner()
            }
        }
    }

    private fun init(binding: FragmentPatientSignupBinding) {
        val spannable = SpannableString(getString(R.string.already_sign_up_login))
        binding.loginTv.text = spannable.underline(16, spannable.length)
    }

    private fun addObserver() {
        mViewModel.userResponse.observe(viewLifecycleOwner) {
            it?.let { user ->
                AppPreferences.userDetails = it
                if (user.type == HelperConstant.UserType.PATIENT.name) {
                    activity?.startActivity<PatientDashboardActivity>()
                } else {
                    activity?.startActivity<DoctorDashboardActivity>()
                }
                activity?.finish()
            }
        }
    }

    private fun setClickListener(binding: FragmentPatientSignupBinding) {
        binding.signUpBt.setOnClickListener {
            if (validateSignupForm(mViewModel.nameLiveData.value,
                    mViewModel.emailLiveData.value,
                    mViewModel.passwordLiveData.value,
                    binding.confirmPassword.text.toString().trim())
            ) {
                binding.specialitySpinner.selectedItem?.toString()?.let {
                    mViewModel.specialityLiveData.value = it
                }

                mViewModel.signUp()
            }
        }
        binding.loginTv.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun getViewModel(): LoginRegisterViewModel = mViewModel

    private fun validateSignupForm(
        name: String?, email: String?, password: String?, confirmPassword: String,
    ): Boolean {
        when {
            name.isNullOrEmpty() -> {
                activity?.showToast(getString(R.string.enter_name))
                return false
            }
            !AppValidator.isValidName(name) -> {
                activity?.showToast(getString(R.string.name_at_least_two_character))
                return false
            }

            email.isNullOrEmpty() -> {
                activity?.showToast(getString(R.string.enter_email))
                return false
            }
            !AppValidator.isValidEmail(email) -> {
                activity?.showToast(getString(R.string.enter_a_valid_email))
                return false
            }
            password.isNullOrEmpty() -> {
                activity?.showToast(getString(R.string.enter_password))
                return false
            }
            !AppValidator.isValidPassword(password) -> {
                activity?.showToast(getString(R.string.enter_a_valid_password))
                return false
            }
            password != confirmPassword -> {
                activity?.showToast(getString(R.string.password_mismatch))
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun setDoctorSpecialitySpinner() {
        with(getViewDataBinding()) {
            val specialityListAdapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.doctorSpecialityList,
                android.R.layout.simple_spinner_item)
            specialityListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            specialitySpinner.adapter = specialityListAdapter
        }

    }

}