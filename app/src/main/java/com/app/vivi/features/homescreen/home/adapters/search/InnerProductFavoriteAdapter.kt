package com.app.vivi.features.homescreen.home.adapters.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.response.NewFavoriteProduct
import com.app.vivi.databinding.ItemInnerRecyclerviewBinding
import com.app.vivi.databinding.ProductItemInnerFavoriteBinding
import roundLeftCorners

class InnerProductFavoriteAdapter :
    RecyclerView.Adapter<InnerProductFavoriteAdapter.InnerViewHolder>() {

    private var items: List<NewFavoriteProduct> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ProductItemInnerFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InnerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // Update the list with DiffUtil
    fun submitList(newItems: List<NewFavoriteProduct>) {
        val diffCallback = InnerDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class InnerViewHolder(private val binding: ProductItemInnerFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: NewFavoriteProduct) {
            binding.tvTitle1.text = product.name
            binding.tvDescription1.text = product.searchType
        }
    }

    // DiffUtil Callback for calculating item differences
    class InnerDiffCallback(
        private val oldList: List<NewFavoriteProduct>,
        private val newList: List<NewFavoriteProduct>
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
