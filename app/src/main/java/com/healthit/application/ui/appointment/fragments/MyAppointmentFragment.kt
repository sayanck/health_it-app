package com.healthit.application.ui.appointment.fragments

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.databinding.FragmentPatientAppointmentBinding
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.ui.appointment.adapter.MyAppointmentsAdapter
import com.healthit.application.ui.doctor.patientDetail.PatientDetailActivity
import com.healthit.application.ui.patient.dashboard.PatientMainViewModel
import com.healthit.application.ui.videoCall.VideoCallActivity
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAppointmentFragment :
    BaseFragment<FragmentPatientAppointmentBinding, PatientMainViewModel>(R.layout.fragment_patient_appointment) {

    private val mViewModel: PatientMainViewModel by viewModels()
    private var mAdapter: MyAppointmentsAdapter? = null
    private var mAppointmentsList = arrayListOf<AppointmentModel?>()

    companion object {
        const val TAG = "MyAppointmentFragment::"
    }

    override fun onInitDataBinding(viewBinding: FragmentPatientAppointmentBinding) {
        init(viewBinding)
        setClickListeners(viewBinding)
        observers()
        swipeToRefreshListener()
        setRecyclerView()
        hitApiBasedOnScreen()
    }

    private fun hitApiBasedOnScreen() {
        if (Navigation.findNavController(getViewDataBinding().root).currentDestination?.id == R.id.patientAppointmentFragment) {
            mViewModel.fetchAllAppointments()
        } else {
            mAdapter?.isFromDoctor(true)
            mViewModel.fetchDoctorAppointments()
        }
    }

    private fun init(viewBinding: FragmentPatientAppointmentBinding) {
        viewBinding.apply {
            appBar.backIv.visibility = View.GONE
            appBar.titleTv.text = getString(R.string.my_appointments)
        }
    }

    private fun observers() {
        mViewModel.appointmentResponse.observe(viewLifecycleOwner) {
            getViewDataBinding().includedSegmentView.segmentGroup.setPosition(0, true)
            getViewDataBinding().swipeToRefresh.apply {
                if (isRefreshing) {
                    this.isRefreshing = false
                }
            }
            it?.let {
                mAppointmentsList = it as ArrayList<AppointmentModel?>
                val upcomingList = mAppointmentsList.filter { appointment ->
                    appointment?.status == HelperConstant.AppointmentStatus.PENDING.name || appointment?.status == HelperConstant.AppointmentStatus.CONFIRMED.name
                }
                submitData(upcomingList)
            }
        }
    }

    private fun submitData(list: List<AppointmentModel?>) {
        if (list.isEmpty()) {
            getViewDataBinding().appointmentsRv.visibility = View.GONE
            getViewDataBinding().noAppointmentTv.visibility = View.VISIBLE
        } else {
            getViewDataBinding().appointmentsRv.visibility = View.VISIBLE
            getViewDataBinding().noAppointmentTv.visibility = View.GONE
            mAdapter?.submitList(list.asReversed().toList())
        }
    }


    private fun setRecyclerView() {
        mAdapter = MyAppointmentsAdapter(false, {
            activity?.startActivity<PatientDetailActivity> {
                putExtra(HelperConstant.IntentParams.sAPPOINTMENTS_DETAIL, it)
            }
        }, {
            activity?.startActivity<VideoCallActivity> { }
        }, {
            showMarkPaidSheet(it)
        })
        getViewDataBinding().appointmentsRv.adapter = mAdapter
    }

    private fun setClickListeners(viewBinding: FragmentPatientAppointmentBinding) {
        with(viewBinding) {
            includedSegmentView.upComingBt.setOnClickListener {
                val upComingList = mAppointmentsList.filter { appointment ->
                    appointment?.status == HelperConstant.AppointmentStatus.PENDING.name || appointment?.status == HelperConstant.AppointmentStatus.CONFIRMED.name
                }
                submitData(upComingList)
            }
            includedSegmentView.activeBt.setOnClickListener {
                val activeList = mAppointmentsList.filter { appointment ->
                    appointment?.status == HelperConstant.AppointmentStatus.INPROGRESS.name
                }
                submitData(activeList)
            }
            includedSegmentView.pastBt.setOnClickListener {
                val pastList = mAppointmentsList.filter { appointment ->
                    appointment?.status == HelperConstant.AppointmentStatus.COMPLETED.name || appointment?.status == HelperConstant.AppointmentStatus.DECLINED.name
                }
                submitData(pastList)
            }
        }
    }

    private fun swipeToRefreshListener() {
        getViewDataBinding().swipeToRefresh.setOnRefreshListener {
            hitApiBasedOnScreen()
            getViewDataBinding().includedSegmentView.segmentGroup.setPosition(0, true)
        }
    }

    private fun showMarkPaidSheet(appointmentModel: AppointmentModel) {
        val bottomSheetFragmentCartUpdated = BottomSheetFragmentMarkAsPaid(appointmentModel) {
            mViewModel.fetchAllAppointments()
        }
        bottomSheetFragmentCartUpdated.show(childFragmentManager, TAG)
    }

    override fun getViewModel(): PatientMainViewModel = mViewModel

}