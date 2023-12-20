package com.healthit.application.ui.doctor.dashboard.home

import android.text.SpannableString
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.data.AppPreferences
import com.healthit.application.databinding.FragmentDoctorHomeBinding
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.ui.doctor.dashboard.DoctorDashboardActivity
import com.healthit.application.ui.doctor.dashboard.DoctorDashboardViewModel
import com.healthit.application.ui.doctor.patientDetail.PatientDetailActivity
import com.healthit.application.ui.videoCall.VideoCallActivity
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.fontSize
import com.healthit.application.utils.fontType
import com.healthit.application.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorHomeFragment :
    BaseFragment<FragmentDoctorHomeBinding, DoctorDashboardViewModel>(R.layout.fragment_doctor_home) {

    private val mViewModel: DoctorDashboardViewModel by activityViewModels()
    private var mAdapter: UpComingAppointmentsAdapter? = null

    override fun onInitDataBinding(viewBinding: FragmentDoctorHomeBinding) {
        init()
        setClickListeners()
        observers()
        setAdapter(viewBinding)
        mViewModel.fetchDoctorAppointments()
    }

    private fun init() {
        AppPreferences.userDetails?.let {
            val spannableString =
                SpannableString(getString(R.string.welcome_doctor).format(it.name.replace("Dr",
                    "").replace(".", "").trim()))
            activity?.let { context ->
                getViewDataBinding().welcomeBackDoctorTv.text =
                    spannableString.fontType(14,
                        spannableString.length,
                        ResourcesCompat.getFont(context, R.font.open_sans_bold))
            }
        }
    }

    private fun observers() {
        mViewModel.appointmentResponse.observe(viewLifecycleOwner) {
            it?.let {
                val confirmedList = it.filter { appointment ->
                    appointment?.status == HelperConstant.AppointmentStatus.CONFIRMED.name
                }
                submitData(confirmedList)
            }
        }
    }

    private fun setClickListeners() {
        with(getViewDataBinding()) {
            setOnItemClick {
                when (it.id) {
                    R.id.patientRequestTv -> {
                        findNavController().navigate(R.id.appointmentRequestFragment)
                    }
                    R.id.patientsTv -> {
                        activity?.startActivity<VideoCallActivity>()
                    }
                    R.id.seeAllTv -> {
                        (activity as DoctorDashboardActivity).changeTab(R.id.doctorAppointmentFragment)
                    }
                }
            }
        }
    }

    private fun submitData(list: List<AppointmentModel?>) {
        if (list.isEmpty()) {
            getViewDataBinding().upComingGroup.visibility = View.GONE
            getViewDataBinding().noAppointmentTv.visibility = View.VISIBLE
        } else {
            getViewDataBinding().upComingGroup.visibility = View.VISIBLE
            getViewDataBinding().noAppointmentTv.visibility = View.GONE
            mAdapter?.submitList(list.asReversed().toList())
        }
    }

    private fun setAdapter(viewBinding: FragmentDoctorHomeBinding) {
        mAdapter = UpComingAppointmentsAdapter {
            activity?.startActivity<PatientDetailActivity> {
                putExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL, it)
            }
        }
        viewBinding.upComingAppointmentsRv.adapter = mAdapter
    }


    override fun getViewModel(): DoctorDashboardViewModel = mViewModel
}