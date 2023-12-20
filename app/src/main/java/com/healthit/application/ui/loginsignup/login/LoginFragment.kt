package com.healthit.application.ui.loginsignup.login

import android.os.Bundle
import android.text.SpannableString
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.data.AppPreferences
import com.healthit.application.databinding.FragmentPatientLoginBinding
import com.healthit.application.ui.patient.dashboard.PatientDashboardActivity
import com.healthit.application.ui.doctor.dashboard.DoctorDashboardActivity
import com.healthit.application.ui.loginsignup.LoginRegisterViewModel
import com.healthit.application.utils.AppValidator
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.showToast
import com.healthit.application.utils.startActivity
import com.healthit.application.utils.underline
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentPatientLoginBinding, LoginRegisterViewModel>(R.layout.fragment_patient_login) {


    companion object {
        const val TAG = "Login"
    }

    private val mViewModel: LoginRegisterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback {
            findNavController().navigate(R.id.doctorPatientChooserFragment)
        }
    }

    override fun onInitDataBinding(viewBinding: FragmentPatientLoginBinding) {
        viewBinding.let {
            init(it)
            setUpClickListener(it)
            addObserver()
        }
    }

    private fun init(binding: FragmentPatientLoginBinding) {
        val spannable = SpannableString(getString(R.string.already_login_signup))
        binding.signUpTv.text = spannable.underline(18, spannable.length)

        val forgotPassword = SpannableString(getString(R.string.forgot_password))
        binding.forgotPasswrodTv.text = forgotPassword.underline(0, forgotPassword.length)
    }

    private fun setUpClickListener(binding: FragmentPatientLoginBinding) {
        binding.loginBt.setOnClickListener {
            if (validateEmailAndPassword(
                    binding.emailEt.text.toString().trim(),
                    binding.passwordEt.text.toString().trim()
                )
            ) {
                mViewModel.login(
                    binding.emailEt.text.toString().trim(),
                    binding.passwordEt.text.toString().trim()
                )
            }
        }
        binding.signUpTv.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }

        binding.forgotPasswrodTv.setOnClickListener {
            activity?.showToast("Coming Soon...")
        }
    }

    override fun getViewModel(): LoginRegisterViewModel = mViewModel

    private fun validateEmailAndPassword(email: String?, password: String?): Boolean {
        when {
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
            else -> {
                return true
            }
        }
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

}