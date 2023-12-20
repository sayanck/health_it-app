package com.healthit.application.ui.patient.doctorDetail.fragments

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.databinding.FragmentDoctorBookingBinding
import com.healthit.application.model.CalenderModel
import com.healthit.application.model.Slot
import com.healthit.application.ui.patient.dashboard.PatientDashboardActivity
import com.healthit.application.ui.patient.doctorDetail.DoctorDetailsViewModel
import com.healthit.application.ui.patient.doctorDetail.adapter.TimeSlotAdapter
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.getDateFromGMT
import com.healthit.application.utils.getTimeFromGMT
import com.healthit.application.utils.showToast
import com.healthit.application.utils.startActivity

class DoctorBookingFragment :
    BaseFragment<FragmentDoctorBookingBinding, DoctorDetailsViewModel>(R.layout.fragment_doctor_booking) {

    companion object {
        const val TAG = "DoctorDetailsFragment::"
    }

    private val mViewModel: DoctorDetailsViewModel by activityViewModels()
    private var mTimeSlotAdapter: TimeSlotAdapter? = null
    private var slotList: ArrayList<Slot> = arrayListOf()
    private var selectedTime: String = HelperConstant.Time.MORNING.name
    private var currentTime: String = ""
    private var selectedDate: CalenderModel? = null
    override fun onInitDataBinding(viewBinding: FragmentDoctorBookingBinding) {
        init(viewBinding)
        setClickListeners()
        observers()
        addSlots()
    }

    private fun init(viewBinding: FragmentDoctorBookingBinding) {
        viewBinding.apply {
            morningInclude.chipBt.text = getString(R.string.morning)
            eveningInclude.chipBt.text = getString(R.string.evening)
        }
        selectMorningOrEvening(HelperConstant.Time.MORNING.name)
    }

    private fun observers() {
        mViewModel.selectedDateLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            getViewDataBinding().dateTagTv.text = getDateFromGMT(selectedDate?.data.toString())
            currentTime = getTimeFromGMT(selectedDate?.data.toString()).uppercase()
            setTimeSlotAdapter()
        }

        mViewModel.bookAppointmentResponse.observe(viewLifecycleOwner) {
            if (it) {
                showRequestSentSheet()
            }
        }
    }

    private fun setClickListeners() {
        with(getViewDataBinding()) {
            includeAppBar.backIv.setOnClickListener {
                it.findNavController().apply {
                    previousBackStackEntry?.savedStateHandle?.set("backFromBooking", true)
                    popBackStack()
                }
            }
            morningInclude.chipBt.setOnClickListener {
                HelperConstant.Time.MORNING.name.apply {
                    if (selectedTime != this) {
                        selectedTime = this
                        selectMorningOrEvening(this)
                    }
                }

            }
            eveningInclude.chipBt.setOnClickListener {
                HelperConstant.Time.EVENING.name.apply {
                    if (selectedTime != this) {
                        selectedTime = this
                        selectMorningOrEvening(this)
                    }
                }

            }

            bookNowBt.setOnClickListener {
                validate()
            }
        }
    }

    private fun selectMorningOrEvening(time: String) {
        when (time) {
            HelperConstant.Time.MORNING.name -> {
                selectMorning()
            }
            HelperConstant.Time.EVENING.name -> {
                selectEvening()
                mTimeSlotAdapter?.selectedSlot = time
            }
        }
        updateTimeSlotAdapter(time)
    }

    private fun selectMorning() {
        with(getViewDataBinding()) {
            activity?.let {
                morningInclude.chipBt.setTextColor(ContextCompat.getColor(it, R.color.white))
                morningInclude.chipBt.background =
                    ContextCompat.getDrawable(it, R.drawable.chip_round_corner_dark_bg)
                morningInclude.chipBt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_morning_white,
                    0,
                    0,
                    0)

                eveningInclude.chipBt.setTextColor(ContextCompat.getColor(it, R.color.blue))
                eveningInclude.chipBt.background =
                    ContextCompat.getDrawable(it, R.drawable.chips_blue_border_bg)
                eveningInclude.chipBt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_evening,
                    0,
                    0,
                    0)
            }
        }
    }

    private fun selectEvening() {
        with(getViewDataBinding()) {
            activity?.let {
                eveningInclude.chipBt.setTextColor(ContextCompat.getColor(it, R.color.white))
                eveningInclude.chipBt.background =
                    ContextCompat.getDrawable(it, R.drawable.chip_round_corner_dark_bg)
                eveningInclude.chipBt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_evening_white,
                    0,
                    0,
                    0)

                morningInclude.chipBt.setTextColor(ContextCompat.getColor(it, R.color.blue))
                morningInclude.chipBt.background =
                    ContextCompat.getDrawable(it, R.drawable.chips_blue_border_bg)
                morningInclude.chipBt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_wb_sunny_24,
                    0,
                    0,
                    0)
            }
        }
    }

    private fun setTimeSlotAdapter() {
        activity?.let {
            mTimeSlotAdapter = TimeSlotAdapter(it) { time ->
                mViewModel.setTimeSlot(time)
            }
            getViewDataBinding().timeSlotRv.adapter = mTimeSlotAdapter
            mTimeSlotAdapter?.selectedDate(selectedDate)
            mTimeSlotAdapter?.submitList(slotList.toMutableList())
        }
    }

    private fun addSlots() {
        slotList = arrayListOf(
            Slot("09:00 AM", HelperConstant.Time.MORNING.name),
            Slot("10:00 AM", HelperConstant.Time.MORNING.name),
            Slot("11:00 AM", HelperConstant.Time.MORNING.name),
            Slot("12:00 PM", HelperConstant.Time.MORNING.name),
            Slot("14:00 PM", HelperConstant.Time.EVENING.name),
            Slot("15:00 PM", HelperConstant.Time.EVENING.name),
            Slot("16:00 PM", HelperConstant.Time.EVENING.name),
            Slot("17:00 PM", HelperConstant.Time.EVENING.name),
            Slot("18:00 PM", HelperConstant.Time.EVENING.name),
            Slot("19:00 PM", HelperConstant.Time.EVENING.name))

    }

    private fun updateTimeSlotAdapter(time: String) {
        mTimeSlotAdapter?.selectedItemPos = -1
        mTimeSlotAdapter?.selectedSlot = time
        mTimeSlotAdapter?.submitList(slotList.toMutableList())
        mViewModel.setTimeSlot(null)
    }

    private fun validate() {
        if (mViewModel.slot.value == null) {
            activity?.showToast(getString(R.string.please_select_time_slot))
        } else if (getViewDataBinding().writeYourProblemEt.text?.toString()?.trim()
                .isNullOrEmpty()
        ) {
            activity?.showToast(getString(R.string.describe_your_problem))
        } else {
            mViewModel.bookAppointment()
        }
    }

    private fun showRequestSentSheet() {
        val bottomSheetFragmentCartUpdated = BottomSheetFragmentRequestSent {
            activity?.startActivity<PatientDashboardActivity>()
        }
        bottomSheetFragmentCartUpdated.show(childFragmentManager, TAG)
    }

    override fun getViewModel(): DoctorDetailsViewModel = mViewModel
}