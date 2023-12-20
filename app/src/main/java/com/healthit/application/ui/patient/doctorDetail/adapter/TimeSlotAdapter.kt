package com.healthit.application.ui.patient.doctorDetail.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.healthit.application.R
import com.healthit.application.base.recyclerViewAdapter.BaseAdapter
import com.healthit.application.base.recyclerViewAdapter.DataBindingViewHolder
import com.healthit.application.databinding.TimeSlotItemBinding
import com.healthit.application.model.CalenderModel
import com.healthit.application.model.Slot
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.getTimeFromGMT
import com.healthit.application.utils.getTodayDate

class TimeSlotAdapter(
    private val context: Context,
    private val onClickItem: (Slot) -> Unit,
) :
    BaseAdapter<Slot>(
        itemsSame = { _, _ -> false },
        contentsSame = { _, _ -> false }
    ) {

    var selectedItemPos = -1
    var selectedSlot = HelperConstant.Time.MORNING.name
    var currentTime = ""
    var selectedDate: CalenderModel? = null

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.time_slot_item
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<Slot>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        val binding = holder.binding as TimeSlotItemBinding

        if (position == selectedItemPos) {
            setSelected(binding)
        } else {
            setUnSelected(binding)
        }

        if (selectedSlot == item.slotTiming && (selectedDate?.data?.after(getTodayDate()) == true || item.timeString > currentTime)) {
            binding.chipBt.isEnabled = true
            binding.chipBt.alpha = 1f
        } else {
            binding.chipBt.isEnabled = false
            binding.chipBt.alpha = 0.5f
        }

        binding.chipBt.text = item.timeString

        binding.chipBt.setOnClickListener {
            setSelected(binding)
            if (selectedItemPos != position) {
                notifyItemChanged(selectedItemPos)
                selectedItemPos = position
            }
            onClickItem(item)
        }
    }

    private fun setSelected(binding: TimeSlotItemBinding) {
        binding.chipBt.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            background = ContextCompat.getDrawable(context,
                R.drawable.chip_round_corner_dark_bg
            )
        }
    }

    private fun setUnSelected(binding: TimeSlotItemBinding) {
        binding.chipBt.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.blue
                )
            )

            background = ContextCompat.getDrawable(context,
                R.drawable.chips_blue_border_bg
            )
        }
    }

    fun setTime(time: String) {
        currentTime = time
    }


    fun selectedDate(date: CalenderModel?) {
        selectedDate = date
        currentTime = getTimeFromGMT(date?.data.toString()).uppercase()
    }

}