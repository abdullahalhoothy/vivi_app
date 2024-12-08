package com.app.vivi.features.homescreen.home.adapters.search

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.data.remote.model.response.products
import com.app.vivi.data.remote.model.response.searchfragment.ProductType
import com.app.vivi.databinding.ItemInnerRecyclerviewBinding
import com.app.vivi.extension.applyRandomDarkGradient
import loadImageWithCache
import roundLeftCorners

class InnerAdapter(private val onItemClick: (item: ProductType) -> Unit) : RecyclerView.Adapter<InnerAdapter.InnerViewHolder>() {

    private var items: List<ProductType> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ItemInnerRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // Update the list with DiffUtil
    fun submitList(newItems: List<ProductType>) {
        val diffCallback = InnerDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class InnerViewHolder(private val binding: ItemInnerRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:ProductType) {
            binding.textView.text = item.type
            binding.clMain.applyRandomDarkGradient()
            binding.imageView.roundLeftCorners(20f)

            item?.imageUrl?.let { binding.imageView.loadImageWithCache(it, R.drawable.ic_bg_coffee) }

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    // DiffUtil Callback for calculating item differences
    class InnerDiffCallback(
        private val oldList: List<ProductType>,
        private val newList: List<ProductType>
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
