package com.app.vivi.features.homescreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.response.NewFavoriteProduct
import com.app.vivi.databinding.ProductItemOuterFavoriteBinding
import com.app.vivi.features.homescreen.home.adapters.ProductOuterFavoriteAdapter.ProductViewHolder
import com.app.vivi.features.homescreen.home.adapters.search.InnerProductFavoriteAdapter

class ProductOuterFavoriteAdapter : RecyclerView.Adapter<ProductViewHolder>() {

    private var items: List<List<NewFavoriteProduct>> = emptyList()

    // Create ViewHolder by inflating the XML layout using ViewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemOuterFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // ViewHolder class using ViewBinding
    class ProductViewHolder(private val binding: ProductItemOuterFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(innerItems: List<NewFavoriteProduct>) {
            val innerAdapter = InnerProductFavoriteAdapter()
            binding.rvInner.layoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
            binding.rvInner.adapter = innerAdapter
            binding.rvInner.isNestedScrollingEnabled = false
            innerAdapter.submitList(innerItems) // Use the InnerAdapter's submitList to update items
        }
        // Bind product data to views

    }

    // Update the list with DiffUtil
    fun submitList(newItems: List<List<NewFavoriteProduct>>) {
        val diffCallback = OuterDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    // DiffUtil Callback for OuterAdapter
    class OuterDiffCallback(
        private val oldList: List<List<NewFavoriteProduct>>,
        private val newList: List<List<NewFavoriteProduct>>
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
