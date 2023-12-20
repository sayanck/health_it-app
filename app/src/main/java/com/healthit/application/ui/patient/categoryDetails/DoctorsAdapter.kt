package com.healthit.application.ui.patient.categoryDetails

import com.healthit.application.R
import com.healthit.application.base.recyclerViewAdapter.BaseAdapter
import com.healthit.application.base.recyclerViewAdapter.DataBindingViewHolder
import com.healthit.application.databinding.DoctorItemBinding
import com.healthit.application.model.response.userDetails.User

class DoctorsAdapter(
    private val onClickItem: (User) -> Unit,
) :
    BaseAdapter<User>(
        itemsSame = { old, new -> old == new },
        contentsSame = { old, new -> old == new }
    ) {
    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.doctor_item
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<User>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        val binding = holder.binding as DoctorItemBinding
        binding.doctorNameTv.text = item.name
        binding.doctorEmailTv.text = item.email
        binding.root.setOnClickListener {
            onClickItem(item)
        }
    }
}