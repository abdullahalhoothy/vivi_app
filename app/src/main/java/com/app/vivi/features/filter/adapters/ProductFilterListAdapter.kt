package com.app.vivi.features.filter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.data.filter.ProductFilterData
import com.app.vivi.databinding.ProductFilterListItemBinding
import com.app.vivi.helper.cutOnText

// Adapter class
class ProductFilterListAdapter(private val context: Context) :
    ListAdapter<ProductFilterData, ProductFilterListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductFilterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(private val binding: ProductFilterListItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(product: ProductFilterData) {
            binding.apply {
                // Bind data to views
                ivProductBackground.setImageResource(product.backgroundImage)
                ivBottle.setImageResource(product.bottleImage)
                tvDiscount.text = product.discountPrice
                tvOrginalPrice.text =
                    cutOnText(context, "CA$${product?.originalPrice}")
                tvProductName.text = product.name
                tvProductDetails.text = product.description
                tvProductAddress.text = product.address
                ratingText.text = product.rating.toString()
                ratingBar.rating = product.rating
                ratingsCount.text = product.ratingsCount
                ivRatingUser.setImageResource(product.userImage)
                tvRatingUser.text = product.ratingUser
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductFilterData>() {
        override fun areItemsTheSame(oldItem: ProductFilterData, newItem: ProductFilterData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductFilterData, newItem: ProductFilterData): Boolean {
            return oldItem == newItem
        }
    }
}