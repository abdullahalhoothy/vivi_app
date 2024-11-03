package com.app.vivi.features.homescreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.data.productfragment.Product
import com.app.vivi.data.remote.model.response.NewFavoriteProduct
import com.app.vivi.databinding.ProductItemFavoriteBinding

class ProductAdapter : ListAdapter<NewFavoriteProduct, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    // ViewHolder class using ViewBinding
    class ProductViewHolder(private val binding: ProductItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind product data to views
        fun bind(product: NewFavoriteProduct) {
            binding.tvTitle1.text = product.name
            binding.tvDescription1.text = product.searchType
            binding.tvTitle2.text = product.name
            binding.tvDescription2.text = product.searchType
            binding.tvTitle3.text = product.name
            binding.tvDescription3.text = product.searchType
        }
    }

    // Create ViewHolder by inflating the XML layout using ViewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    // DiffUtil to efficiently handle list updates
    class ProductDiffCallback : DiffUtil.ItemCallback<NewFavoriteProduct>() {
        override fun areItemsTheSame(oldItem: NewFavoriteProduct, newItem: NewFavoriteProduct): Boolean {
            // Compare unique properties of NewFavoriteProduct (assuming 'id' is a unique identifier)
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NewFavoriteProduct, newItem: NewFavoriteProduct): Boolean {
            // Compare all the fields of NewFavoriteProduct
            return oldItem == newItem
        }
    }
}
