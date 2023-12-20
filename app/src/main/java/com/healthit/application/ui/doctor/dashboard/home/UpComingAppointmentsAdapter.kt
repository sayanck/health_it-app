package com.healthit.application.ui.doctor.dashboard.home

import com.healthit.application.R
import com.healthit.application.base.recyclerViewAdapter.BaseAdapter
import com.healthit.application.base.recyclerViewAdapter.DataBindingViewHolder
import com.healthit.application.databinding.DoctorsCategoryItemBinding
import com.healthit.application.databinding.HomeUpcomingAppointmentsItemBinding
import com.healthit.application.model.DoctorsCategory
import com.healthit.application.model.request.AppointmentModel

class UpComingAppointmentsAdapter(
    private val onClickItem: (AppointmentModel) -> Unit,
) :
    BaseAdapter<AppointmentModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> old == new }
    ) {
    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.home_upcoming_appointments_item
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<AppointmentModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        val binding = holder.binding as HomeUpcomingAppointmentsItemBinding
        binding.patientNameTv.text = item.patientName
        binding.timeTv.text = item.startTime?.substringAfter(" ")
        binding.root.setOnClickListener {
            onClickItem(item)
        }
    }
}