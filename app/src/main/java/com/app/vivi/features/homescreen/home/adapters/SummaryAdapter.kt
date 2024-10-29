package com.app.vivi.features.homescreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.data.productfragment.SummaryData
import com.app.vivi.databinding.SummaryItemBinding

class SummaryAdapter(private val onItemClick: (item: SummaryData) -> Unit) :
    ListAdapter<SummaryData, SummaryAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: SummaryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SummaryData) {
            binding.apply {
                ivEstimatedTime.setImageResource(item.imageResId)
                tvEstimatedTime.text = item.description

                // Handle item click
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SummaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<SummaryData>() {
        override fun areItemsTheSame(oldItem: SummaryData, newItem: SummaryData): Boolean {
            // Compare unique identifiers of items (e.g., ID)
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SummaryData, newItem: SummaryData): Boolean {
            // Compare contents for equality
            return oldItem == newItem
        }
    }
}
