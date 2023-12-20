package com.healthit.application.ui.doctor.patientAppointmentRequest.adapter

import com.healthit.application.R
import com.healthit.application.base.recyclerViewAdapter.BaseAdapter
import com.healthit.application.base.recyclerViewAdapter.DataBindingViewHolder
import com.healthit.application.databinding.AppointmentRequestItemBinding
import com.healthit.application.model.request.AppointmentModel


class PatientAppointmentRequestAdapter(
    private val clickEvent: (ClickEvent<AppointmentModel>) -> Unit,
) :
    BaseAdapter<AppointmentModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> old == new }
    ) {
    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.appointment_request_item
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<AppointmentModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        val binding = holder.binding as AppointmentRequestItemBinding
        binding.patientNameTv.text = item.patientName
        binding.dateTv.text = item.startTime?.substringBefore(" ")
        binding.timeTv.text = item.startTime?.substringAfter(" ")
        binding.confirmIv.setOnClickListener {
            clickEvent(ClickEvent.Confirm(item))
        }

        binding.declineIv.setOnClickListener {
            clickEvent(ClickEvent.Decline(item))
        }

        binding.root.setOnClickListener {
            clickEvent(ClickEvent.Click(item))
        }
    }
}

sealed class ClickEvent<out T> {
    data class Click<out T>(val data: T) : ClickEvent<T>()
    data class Confirm<out T>(val data: T) : ClickEvent<T>()
    data class Decline<out T>(val data: T) : ClickEvent<T>()
}