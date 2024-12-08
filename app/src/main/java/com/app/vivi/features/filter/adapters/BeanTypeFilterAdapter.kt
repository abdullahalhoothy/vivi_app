package com.app.vivi.features.filter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.data.remote.model.response.filter.CoffeeBeanType
import com.app.vivi.databinding.ItemFilterTagBinding

class BeanTypeFilterAdapter(
    private val onItemClicked: (CoffeeBeanType) -> Unit
) : ListAdapter<CoffeeBeanType, BeanTypeFilterAdapter.ViewHolder>(CoffeeBeanTypeDiffCallback()) {

    fun unselectAll() {
        val updatedList = currentList.map { it.copy(isSelected = false) }
        submitList(updatedList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterTagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(
        private val context: Context,
        private val binding: ItemFilterTagBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: CoffeeBeanType, position: Int) {
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

class CoffeeBeanTypeDiffCallback : DiffUtil.ItemCallback<CoffeeBeanType>() {
    override fun areItemsTheSame(oldItem: CoffeeBeanType, newItem: CoffeeBeanType): Boolean {
        // Compare unique identifiers (e.g., names or IDs)
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: CoffeeBeanType, newItem: CoffeeBeanType): Boolean {
        // Compare all fields to detect content changes
        return oldItem == newItem
    }
}