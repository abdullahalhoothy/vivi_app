package com.app.vivi.features.homescreen.home.adapters.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.databinding.ItemOuterBinding

class OuterAdapter : RecyclerView.Adapter<OuterAdapter.OuterViewHolder>() {

    private var items: List<List<String>> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
        val binding = ItemOuterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OuterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // Update the list with DiffUtil
    fun submitList(newItems: List<List<String>>) {
        val diffCallback = OuterDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class OuterViewHolder(private val binding: ItemOuterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(innerItems: List<String>) {
            val innerAdapter = InnerAdapter()
            binding.rvInner.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
            binding.rvInner.adapter = innerAdapter
            binding.rvInner.isNestedScrollingEnabled = false
            innerAdapter.submitList(innerItems) // Use the InnerAdapter's submitList to update items
        }
    }

    // DiffUtil Callback for OuterAdapter
    class OuterDiffCallback(
        private val oldList: List<List<String>>,
        private val newList: List<List<String>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Compare outer lists based on their contents
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Additional checks for content equality if needed
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
