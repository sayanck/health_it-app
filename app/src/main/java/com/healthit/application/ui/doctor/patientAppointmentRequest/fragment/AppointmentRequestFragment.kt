package com.healthit.application.ui.doctor.patientAppointmentRequest.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.databinding.FragmentAppointmentRequestBinding
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.ui.doctor.dashboard.DoctorDashboardViewModel
import com.healthit.application.ui.doctor.patientAppointmentRequest.adapter.ClickEvent
import com.healthit.application.ui.doctor.patientAppointmentRequest.adapter.PatientAppointmentRequestAdapter
import com.healthit.application.ui.doctor.patientDetail.PatientDetailActivity
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentRequestFragment :
    BaseFragment<FragmentAppointmentRequestBinding, DoctorDashboardViewModel>(R.layout.fragment_appointment_request) {

    private val mViewModel: DoctorDashboardViewModel by viewModels()
    private var mAdapter: PatientAppointmentRequestAdapter? = null


    override fun onInitDataBinding(viewBinding: FragmentAppointmentRequestBinding) {
        init(viewBinding)
        observers()
        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.fetchDoctorAppointments()
    }


    private fun init(viewBinding: FragmentAppointmentRequestBinding) {
        viewBinding.apply {
            appBar.backIv.visibility = View.VISIBLE
            appBar.titleTv.text = getString(R.string.appointment_requests)
            appBar.backIv.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun observers() {
        mViewModel.appointmentResponse.observe(viewLifecycleOwner) {
            it?.let {
                val confirmedList = it.filter { appointment ->
                    appointment?.status == HelperConstant.AppointmentStatus.PENDING.name
                }
                submitData(confirmedList)
            }
        }

        mViewModel.updateAppointmentResponse.observe(viewLifecycleOwner) {
            mViewModel.fetchDoctorAppointments()
        }
    }

    private fun submitData(list: List<AppointmentModel?>) {
        if (list.isEmpty()) {
            getViewDataBinding().appointmentRequestRv.visibility = View.GONE
            getViewDataBinding().noResultGroup.visibility = View.VISIBLE
        } else {
            getViewDataBinding().appointmentRequestRv.visibility = View.VISIBLE
            getViewDataBinding().noResultGroup.visibility = View.GONE
            mAdapter?.submitList(list.asReversed().toList())
        }
    }


    private fun setRecyclerView() {
        mAdapter = PatientAppointmentRequestAdapter {
            when (it) {
                is ClickEvent.Click -> {
                    activity?.startActivity<PatientDetailActivity> {
                        putExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL, it.data)
                    }
                }
                is ClickEvent.Confirm -> {
                    // handle confirm click
                    val appointmentData = it.data
                    appointmentData.status = HelperConstant.AppointmentStatus.CONFIRMED.name
                    mViewModel.confirmAndDecline(appointmentData)
                }
                is ClickEvent.Decline -> {
                    // handle decline click
                    val appointmentData = it.data
                    appointmentData.status = HelperConstant.AppointmentStatus.DECLINED.name
                    mViewModel.confirmAndDecline(appointmentData)
                }
            }
        }
        getViewDataBinding().appointmentRequestRv.adapter = mAdapter
    }


    override fun getViewModel(): DoctorDashboardViewModel = mViewModel

}