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
import com.app.vivi.data.remote.model.response.filter.CoffeeType
import com.app.vivi.databinding.ItemFilterTagBinding
import com.app.vivi.extension.setDrawableWithSize

class FilterAdapter(
    private val onItemClicked: (CoffeeType) -> Unit
) : ListAdapter<CoffeeType, FilterAdapter.FilterViewHolder>(FilterTagDiffCallback()) {

    // Method to unselect all items
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
        holder.bind(getItem(position))
    }

    inner class FilterViewHolder(
        private val context: Context,
        private val binding: ItemFilterTagBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: CoffeeType) {
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

            val tintColor = ContextCompat.getColor(
                context,
                if (tag.isSelected) R.color.colorWhite else R.color.colorBlack
            )
//            binding.tvFilter.setDrawableW ithSize(context, tag.icon, 50, 50, 5, tintColor)

            binding.root.setOnClickListener {
                val updatedItem = tag.copy(isSelected = !tag.isSelected)
                val updatedList = currentList.toMutableList()
                updatedList[adapterPosition] = updatedItem
                submitList(updatedList)
                onItemClicked(updatedItem)
            }
        }
    }
}

class FilterTagDiffCallback : DiffUtil.ItemCallback<CoffeeType>() {
    override fun areItemsTheSame(oldItem: CoffeeType, newItem: CoffeeType): Boolean {
        // Compare unique identifiers
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CoffeeType, newItem: CoffeeType): Boolean {
        // Compare all fields to detect content changes
        return oldItem == newItem
    }
}