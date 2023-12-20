package com.healthit.application.ui.patient.dashboard.home

import com.healthit.application.R
import com.healthit.application.base.recyclerViewAdapter.BaseAdapter
import com.healthit.application.base.recyclerViewAdapter.DataBindingViewHolder
import com.healthit.application.databinding.DoctorsCategoryItemBinding
import com.healthit.application.model.DoctorsCategory

class DoctorCategoriesAdapter(
    private val onClickItem: (DoctorsCategory) -> Unit,
) :
    BaseAdapter<DoctorsCategory>(
        itemsSame = { old, new -> old == new },
        contentsSame = { old, new -> old == new }
    ) {
    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.doctors_category_item
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<DoctorsCategory>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        val binding = holder.binding as DoctorsCategoryItemBinding
        binding.categoryIv.setImageResource(item.drawable)
        binding.categoryIv.setOnClickListener {
            onClickItem(item)
        }
    }
}