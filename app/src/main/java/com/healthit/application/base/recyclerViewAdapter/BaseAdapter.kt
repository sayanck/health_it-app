package com.healthit.application.base.recyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<T : Any>(
    itemsSame: (T, T) -> Boolean,
    contentsSame: (T, T) -> Boolean,
) :
    ListAdapter<T, DataBindingViewHolder<T>>(
        object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(old: T, new: T): Boolean = itemsSame(old, new)
            override fun areContentsTheSame(old: T, new: T): Boolean = contentsSame(old, new)
        }
    ) {

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    protected abstract fun getLayoutIdForPosition(position: Int): Int
}