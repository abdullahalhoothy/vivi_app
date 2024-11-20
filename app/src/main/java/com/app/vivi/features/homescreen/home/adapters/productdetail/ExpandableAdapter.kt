package com.app.vivi.features.homescreen.home.adapters.productdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.data.productdetailfragment.ThoughtsData
import com.app.vivi.databinding.ItemShowMoreThoughtsLayoutBinding
import com.app.vivi.databinding.ItemThoughtsLayoutBinding

class ExpandableAdapter : ListAdapter<ThoughtsData, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_BUTTON = 1
    private var isExpanded = false

    override fun getItemViewType(position: Int): Int {
        return if (position < getVisibleItemsCount()) VIEW_TYPE_ITEM else VIEW_TYPE_BUTTON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemThoughtsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else {
            val binding = ItemShowMoreThoughtsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ButtonViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = getItem(position) // Using getItem(position) to get the current item
            holder.bind(item)
        } else if (holder is ButtonViewHolder) {

            // Set the button text based on whether the list is expanded or not
//            holder.binding.buttonShowMore.text = if (isExpanded) "Show Less" else "Show More"
            holder.binding.buttonShowMore.visibility = if (isExpanded) View.GONE else View.VISIBLE

            // Set up the button click listener
            holder.binding.buttonShowMore.setOnClickListener {
//                isExpanded = !isExpanded  // Toggle the expanded state
                isExpanded = true
                updateItems() // Update the list to reflect the expanded/collapsed state
            }
        }
    }

    override fun getItemCount(): Int {
        return getVisibleItemsCount() + 1 // Adding 1 for the "Show More/Show Less" button
    }

    private fun getVisibleItemsCount(): Int {
        return if (isExpanded) currentList.size else minOf(3, currentList.size)
    }

    private fun updateItems() {
        // When expanded, show the full list; otherwise, show the first 3 items
        val newItems = if (isExpanded) currentList else currentList.take(3)
        submitList(newItems) // Submit the updated list to refresh the adapter
        // Instead of just submitting the list, notify that the item count changed.
        notifyItemChanged(newItems.size) // This can notify the "Show More" button's state
    }

    inner class ItemViewHolder(private val binding: ItemThoughtsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ThoughtsData) {
            binding.textViewItemTitle.text = item.title
            binding.textViewItemDescription.text = item.description
        }
    }

    inner class ButtonViewHolder(val binding: ItemShowMoreThoughtsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    // DiffUtil callback for calculating the differences between items
    class ItemDiffCallback : DiffUtil.ItemCallback<ThoughtsData>() {
        override fun areItemsTheSame(oldItem: ThoughtsData, newItem: ThoughtsData): Boolean {
            return oldItem.title == newItem.title // Assuming `Item` has an `id` field
        }

        override fun areContentsTheSame(oldItem: ThoughtsData, newItem: ThoughtsData): Boolean {
            return oldItem == newItem
        }
    }
}
