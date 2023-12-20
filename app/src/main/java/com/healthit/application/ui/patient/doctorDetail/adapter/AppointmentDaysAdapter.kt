package com.healthit.application.ui.patient.doctorDetail.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.healthit.application.R
import com.healthit.application.base.recyclerViewAdapter.BaseAdapter
import com.healthit.application.base.recyclerViewAdapter.DataBindingViewHolder
import com.healthit.application.databinding.DayItemBinding
import com.healthit.application.model.CalenderModel

class AppointmentDaysAdapter(
    private val context: Context,
    private val onClickItem: (CalenderModel) -> Unit,
) :
    BaseAdapter<CalenderModel>(
        itemsSame = { _, _ -> false },
        contentsSame = { _, _ -> false }
    ) {

    var selectedItemPos = 0

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.day_item
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<CalenderModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        val binding = holder.binding as DayItemBinding

        if (position == selectedItemPos) {
            setSelected(binding)
        } else {
            setUnSelected(binding)
        }

        binding.dayNameTv.text = item.calendarDay
        binding.dateTv.text = item.calendarDate

        binding.dayCard.setOnClickListener {
            setSelected(binding)
            if (selectedItemPos != position) {
                notifyItemChanged(selectedItemPos)
                selectedItemPos = position
            }
            onClickItem(item)
        }
    }

    private fun setSelected(binding: DayItemBinding) {
        binding.dayNameTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )
        binding.dateTv.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        )
        binding.dayCard.background = ContextCompat.getDrawable(context,
            R.drawable.round_corner_dark_blue_bg
        )
    }

    private fun setUnSelected(binding: DayItemBinding) {
        binding.dayNameTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.blue
            )
        )
        binding.dateTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.blue
            )
        )
        binding.dayCard.background = ContextCompat.getDrawable(context,
            R.drawable.round_corner_blue_border_bg
        )
    }

}