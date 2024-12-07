package com.app.vivi.features.filter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.data.remote.model.data.filter.TagData
import com.app.vivi.data.remote.model.response.filter.Country
import com.app.vivi.databinding.ItemFilterTagBinding

class CountryFilterAdapter(
    private val onItemClicked: (Country) -> Unit
) : ListAdapter<Country, CountryFilterAdapter.FilterViewHolder>(TagDiffCallback()) {

    fun unselectAll() {
        val updatedList = currentList.map { it.copy(isSelected = false) }
        submitList(updatedList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterTagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilterViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class FilterViewHolder(
        private val context: Context,
        private val binding: ItemFilterTagBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: Country, position: Int) {
            binding.tvFilter.text = tag.name
            binding.tvFilter.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (tag.isSelected) R.color.colorWhite else R.color.colorBlack
                )
            )

            if (tag.isSelected) {
                binding.flMain.isSelected = true
                binding.flMain.setBackgroundResource(R.drawable.unselected_tag_background)
            } else {
                binding.flMain.isSelected = false
                binding.flMain.setBackgroundResource(R.drawable.selected_tag_background)
            }

            binding.root.setOnClickListener {
                val updatedItem = tag.copy(isSelected = !tag.isSelected)
                val updatedList = currentList.toMutableList()
                updatedList[position] = updatedItem
                submitList(updatedList)
                onItemClicked(updatedItem)
            }
        }
    }
}

class TagDiffCallback : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        // Compare unique identifiers (e.g., names or IDs)
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        // Compare all fields to detect content changes
        return oldItem == newItem
    }
}