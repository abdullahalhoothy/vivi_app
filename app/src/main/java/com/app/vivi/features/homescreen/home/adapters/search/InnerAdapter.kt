package com.app.vivi.features.homescreen.home.adapters.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.databinding.ItemInnerRecyclerviewBinding

class InnerAdapter : RecyclerView.Adapter<InnerAdapter.InnerViewHolder>() {

    private var items: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ItemInnerRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // Update the list with DiffUtil
    fun submitList(newItems: List<String>) {
        val diffCallback = InnerDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class InnerViewHolder(private val binding: ItemInnerRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.textView.text = item
            // Optionally, set imageView's image source based on item
        }
    }

    // DiffUtil Callback for calculating item differences
    class InnerDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Assuming items have unique identifiers, here items are compared by content
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Additional checks for content equality, if needed
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
