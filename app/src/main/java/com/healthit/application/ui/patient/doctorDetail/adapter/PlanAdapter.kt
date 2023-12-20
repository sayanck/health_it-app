package com.healthit.application.ui.patient.doctorDetail.adapter


import android.content.Context
import androidx.core.content.ContextCompat
import com.healthit.application.R
import com.healthit.application.base.recyclerViewAdapter.BaseAdapter
import com.healthit.application.base.recyclerViewAdapter.DataBindingViewHolder
import com.healthit.application.databinding.PlanPriceItemBinding
import com.healthit.application.model.PlanPriceModel

class PlanAdapter(
    private val context: Context,
    private val onClickItem: (PlanPriceModel) -> Unit,
) :
    BaseAdapter<PlanPriceModel>(
        itemsSame = { old, new -> old == new },
        contentsSame = { old, new -> old == new }
    ) {

    private var selectedItemPos = -1

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.plan_price_item
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<PlanPriceModel>, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)
        val binding = holder.binding as PlanPriceItemBinding

        if (position == selectedItemPos) {
            setSelected(binding, item)
        } else {
            setUnSelected(binding, item)
        }



        binding.root.setOnClickListener {
            setSelected(binding, getItem(position))
            if (selectedItemPos != position) {
                notifyItemChanged(selectedItemPos)
                selectedItemPos = position
            }
            onClickItem(item)
        }
    }

    private fun setSelected(binding: PlanPriceItemBinding, item: PlanPriceModel) {
        binding.apply {
            titleTv.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            descriptionTv.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )

            priceTv.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            doctorIv.setImageDrawable(ContextCompat.getDrawable(context, item.selectedIcon))
        }
    }

    private fun setUnSelected(binding: PlanPriceItemBinding, item: PlanPriceModel) {
        binding.apply {
            titleTv.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.dark_blue
                )
            )
            descriptionTv.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.dark_blue
                )
            )

            priceTv.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.dark_blue
                )
            )
            doctorIv.setImageDrawable(ContextCompat.getDrawable(context, item.unselectedIcon))
        }
    }

}