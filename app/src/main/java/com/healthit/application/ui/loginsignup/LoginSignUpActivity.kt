package com.healthit.application.ui.loginsignup

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.healthit.application.R
import com.healthit.application.base.activity.BaseActivity
import com.healthit.application.databinding.ActivityLoginSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginSignUpActivity :
    BaseActivity<ActivityLoginSignUpBinding, LoginRegisterViewModel>(R.layout.activity_login_sign_up) {

    companion object {
        const val TAG = "LoginRegisterActivity::"
    }

    private lateinit var navController: NavController
    private val mViewModel: LoginRegisterViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_login_signup_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun getBindingVariable(): Int = R.layout.activity_login_sign_up

    override fun onInitDataBinding(viewBinding: ActivityLoginSignUpBinding) {

    }

    override fun getViewModel(): LoginRegisterViewModel = mViewModel

}