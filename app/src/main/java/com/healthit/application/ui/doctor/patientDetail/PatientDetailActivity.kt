package com.healthit.application.ui.doctor.patientDetail

import android.os.Build
import android.view.View
import androidx.activity.viewModels
import com.healthit.application.R
import com.healthit.application.base.activity.BaseActivity
import com.healthit.application.databinding.ActivityPatientDetailBinding
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.model.response.userDetails.User
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.statusColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PatientDetailActivity :
    BaseActivity<ActivityPatientDetailBinding, PatientDetailViewModel>(R.layout.activity_patient_detail) {


    private val mViewModel: PatientDetailViewModel by viewModels()
    private var appointmentModel: AppointmentModel? = null
    private var user: User? = null

    override fun getBindingVariable(): Int = R.layout.activity_patient_detail

    override fun getViewModel(): PatientDetailViewModel = mViewModel


    override fun onInitDataBinding(viewBinding: ActivityPatientDetailBinding) {
        init()
        observer()
        setClickListener(viewBinding)
    }

    private fun setClickListener(viewBinding: ActivityPatientDetailBinding) {
        viewBinding.apply {
            includeAppBar.backIv.setOnClickListener {
                finish()
            }
            acceptBt.setOnClickListener {
                updateStatus(HelperConstant.AppointmentStatus.CONFIRMED.name)
            }
            declineBt.setOnClickListener {
                updateStatus(HelperConstant.AppointmentStatus.DECLINED.name)
            }
        }
    }

    private fun updateStatus(status: String) {
        appointmentModel?.status = status
        appointmentModel?.let {
            mViewModel.confirmAndDecline(it)
        }
    }

    private fun init() {
        appointmentModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL,
                AppointmentModel::class.java)
        } else {
            intent.getParcelableExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL) as AppointmentModel?
        }
        appointmentModel?.patientId?.let {
            mViewModel.fetchUser(it)
        }
    }

    private fun observer() {
        mViewModel.userResponse.observe(this) {
            it?.let {
                user = it
                setData(it)
            }
        }
        mViewModel.updateAppointmentResponse.observe(this) {
            setData(user)
        }
    }

    private fun setData(user: User?) {
        with(getViewDataBinding()) {
            scrollView.visibility = View.VISIBLE
            includeAppBar.titleTv.text = user?.name
            patientNameTv.text = user?.name
            patientEmailTv.text = user?.email
            problemTv.text = appointmentModel?.problemDescription
            timeTv.text = appointmentModel?.startTime?.substringAfter(" ")
            dateTv.text = appointmentModel?.startTime?.substringBefore(" ")
            val formattedStatus =
                getString(R.string.status).format(appointmentModel?.status?.lowercase()
                    ?.replaceFirstChar { firstChar -> firstChar.uppercase() })

            statusTv.text = formattedStatus
            statusTv.setTextColor(this@PatientDetailActivity.statusColor(appointmentModel?.status))
            if (appointmentModel?.status == HelperConstant.AppointmentStatus.PENDING.name) {
                requestGroup.visibility = View.VISIBLE
            } else {
                requestGroup.visibility = View.GONE
            }
        }
    }

}