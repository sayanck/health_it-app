package com.healthit.application.ui.patient.doctorDetail.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.databinding.FragmentDoctorDetailsBinding
import com.healthit.application.model.CalenderModel
import com.healthit.application.ui.patient.doctorDetail.DoctorDetailsViewModel
import com.healthit.application.ui.patient.doctorDetail.adapter.AppointmentDaysAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DoctorDetailsFragment :
    BaseFragment<FragmentDoctorDetailsBinding, DoctorDetailsViewModel>(R.layout.fragment_doctor_details) {

    companion object {
        const val TAG = "DoctorDetailsFragment::"
    }

    private val mViewModel: DoctorDetailsViewModel by activityViewModels()
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    lateinit var calendar: Calendar
    private var mAdapter: AppointmentDaysAdapter? = null
    private val calendarList = ArrayList<CalenderModel>()
    private var isBackFromBooking = false
    private var materialDatePicker: MaterialDatePicker<Long>? = null
    private var firstTimeSelectFromPicker = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calendar = Calendar.getInstance(Locale.getDefault())
        mViewModel.setCurrentDate(CalenderModel(calendar.time))
        mViewModel.setSelectedDate(CalenderModel(calendar.time))
        initDatePicker()
        Log.d(TAG, "onCreateCalled")
    }

    override fun onInitDataBinding(viewBinding: FragmentDoctorDetailsBinding) {

        observer()
        setClickListener()
        setAdapter()
        getDates()
    }

    private fun observer() {
        mViewModel.userLiveData.observe(viewLifecycleOwner) {
            it?.let {
                with(getViewDataBinding()) {
                    includeAppBar.favouriteContainer.visibility = View.VISIBLE
                    includeAppBar.titleTv.text = it.name
                    doctorNameCardTv.text = it.name
                    doctorSpecialityTv.text = it.doctorSpeciality
                    mViewModel.selectedDateLiveData.value?.data?.apply {
                        monthYearPicker.text = sdf.format(this)
                    }
                }
            }
        }
    }

    private fun setAdapter() {
        activity?.let {
            mAdapter = AppointmentDaysAdapter(it) { data ->
                mViewModel.setSelectedDate(data)
            }
            (getViewDataBinding().appointmentDaysRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
                false
            getViewDataBinding().appointmentDaysRv.adapter = mAdapter
        }
    }

    private fun setClickListener() {
        with(getViewDataBinding()) {

            monthYearPicker.setOnClickListener {
                isBackFromBooking = false
                displayDatePicker()
            }
            includeAppBar.backIv.setOnClickListener {
                activity?.finish()
            }

            bookAppointmentBt.setOnClickListener {
                isBackFromBooking = true
                it?.findNavController()?.navigate(R.id.doctorBookingFragment)
            }
        }


    }

    private fun getDates(fromPicker: Boolean = false) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dateList = ArrayList<CalenderModel>() // For our Calendar Data Class
            val dates = ArrayList<Date>() // For Date
            val monthCalendar = Calendar.getInstance()
            val currentDate = mViewModel.currentDateLiveData.value
            val selectedDate = mViewModel.selectedDateLiveData.value
            monthCalendar.time = selectedDate?.data ?: calendar.time
            // find max available days in  month
            val maxDaysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                .minus(getActualDays(fromPicker))

            // set first available day of month
            monthCalendar.set(Calendar.DAY_OF_MONTH,
                if (!fromPicker && !isBackFromBooking) currentDate?.calendarDate?.toInt() ?: 1
                else if (currentDate?.data?.month?.plus(1) == selectedDate?.data?.month?.plus(1)) {
                    currentDate?.calendarDate?.toInt() ?: 1
                } else 1)

            // add days and set to recycler view and set selected position of day in recycler view
            var count = 0
            while (dates.size < maxDaysInMonth) {
                dates.add(monthCalendar.time)
                if (selectedDate?.data == monthCalendar.time) {
                    dateList.add(CalenderModel(monthCalendar.time))
                    mAdapter?.selectedItemPos = count
                } else {
                    dateList.add(CalenderModel(monthCalendar.time))
                }

                monthCalendar.add(Calendar.DAY_OF_MONTH, 1) // Increment Day By 1
                count++
            }

            calendarList.clear()
            calendarList.addAll(dateList)
            mAdapter?.submitList(calendarList.toList())

            //to scroll to selected position
            withContext(Dispatchers.Main) {
                delay(1000)
                getViewDataBinding().appointmentDaysRv.smoothScrollToPosition(mAdapter?.selectedItemPos
                    ?: 0)
            }
        }
    }

    private fun initDatePicker() {
//        val constraintsBuilder =
//            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
//        val materialDateBuilder: MaterialDatePicker.Builder<Long> =
//            MaterialDatePicker.Builder.datePicker().setTheme(R.style.CustomMaterialCalendar)
//                .setCalendarConstraints(constraintsBuilder.build())
//                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//        materialDateBuilder.setTitleText("Select Date")
//
//        materialDatePicker = materialDateBuilder.build()
//        materialDatePicker?.addOnPositiveButtonClickListener {
//            try {
//                Log.d(TAG, "displayDatePicker: $it")
//                Log.d(TAG, "displayDatePicker: ${Date(it)}")
//                getViewDataBinding().monthYearPicker.text = sdf.format(it)
//                mViewModel.setSelectedDate(CalenderModel(Date(it)))
//
//                getDates(true)
//
//
//                Log.d(TAG, "init: Date === ${sdf.format(calendar.time)}")
//
//            } catch (e: ParseException) {
//                Log.d(TAG, "displayDatePicker: ${e.message}")
//            }
//
//        }

    }

    private fun displayDatePicker() {
        val selectTime =
            if (!firstTimeSelectFromPicker) mViewModel.selectedDateLiveData.value?.data?.time?.plus(TimeZone.getDefault().getOffset(mViewModel.selectedDateLiveData.value?.data?.time?:0)) else mViewModel.selectedDateLiveData.value?.data?.time
        val constraintsBuilder =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
        val materialDateBuilder: MaterialDatePicker.Builder<Long> =
            MaterialDatePicker.Builder.datePicker().setTheme(R.style.CustomMaterialCalendar)
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(selectTime)
        materialDateBuilder.setTitleText("Select Date")

        materialDatePicker = materialDateBuilder.build()
        materialDatePicker?.addOnPositiveButtonClickListener {
            try {
                Log.d(TAG, "displayDatePicker: $it")
                Log.d(TAG, "displayDatePicker: ${Date(it)}")
                getViewDataBinding().monthYearPicker.text = sdf.format(it)
                mViewModel.setSelectedDate(CalenderModel(Date(it)))
                firstTimeSelectFromPicker = true

                getDates(true)


                Log.d(TAG, "init: Date === ${sdf.format(calendar.time)}")

            } catch (e: ParseException) {
                Log.d(TAG, "displayDatePicker: ${e.message}")
            }

        }

        materialDatePicker?.show(childFragmentManager, "MATERIAL_DATE_PICKER")
    }

    private fun getActualDays(
        fromPicker: Boolean,
    ): Int {
        return if ((!fromPicker && !isBackFromBooking) || mViewModel.currentDateLiveData.value?.data?.month?.plus(
                1) == mViewModel.selectedDateLiveData.value?.data?.month?.plus(1)
        ) {
            mViewModel.currentDateLiveData.value?.calendarDate?.toInt()?.minus(1) ?: 0
        } else 0

    }


    override fun getViewModel(): DoctorDetailsViewModel = mViewModel

}