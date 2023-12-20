package com.healthit.application.ui.appointment.adapter

import android.view.View
import com.healthit.application.R
import com.healthit.application.base.recyclerViewAdapter.BaseAdapter
import com.healthit.application.base.recyclerViewAdapter.DataBindingViewHolder
import com.healthit.application.databinding.PatientAppointementItemBinding
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.statusColor

class MyAppointmentsAdapter(
    private var fromDoctor: Boolean = false,
    private val onClickItem: (AppointmentModel) -> Unit,
    private val videoCallClicked: () -> Unit,
    private val markAsPaidClicked: (AppointmentModel) -> Unit,
) :
    BaseAdapter<AppointmentModel>(
        itemsSame = { old, new -> old.id == new.id },
        contentsSame = { old, new -> false }
    ) {
    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.patient_appointement_item
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<AppointmentModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        val binding = holder.binding as PatientAppointementItemBinding
        binding.doctorNameCardTv.text = if (fromDoctor) item.patientName else item.doctorName
        val formattedStatus =
            binding.root.context.getString(R.string.status).format(item.status.lowercase()
                .replaceFirstChar { firstChar -> firstChar.uppercase() })

        binding.appointmentStatusTv.text = formattedStatus
        binding.appointmentStatusTv.setTextColor(binding.root.context.statusColor(item.status))
        binding.appointmentTime.text = item.startTime?.substringAfter("")
        binding.markAsPaidBt.visibility =
            if (!fromDoctor && !item.paid && item.status == HelperConstant.AppointmentStatus.CONFIRMED.name) View.VISIBLE else View.GONE
        binding.videoCallContainer.visibility = if (item.paid) View.VISIBLE else View.GONE
        binding.root.setOnClickListener {
            if (fromDoctor)
                onClickItem(item)
        }

        binding.videoCallContainer.setOnClickListener {
            videoCallClicked.invoke()
        }
        binding.markAsPaidBt.setOnClickListener {
            markAsPaidClicked.invoke(item)
        }
    }

    fun isFromDoctor(value: Boolean) {
        fromDoctor = value
    }
}