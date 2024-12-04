package com.app.vivi.features.filter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.data.remote.model.response.filter.FilteredProductsResponse
import com.app.vivi.databinding.ProductFilterListItemBinding
import com.app.vivi.helper.cutOnText
import loadImageWithCache

// Adapter class
class ProductFilterListAdapter(private val context: Context) :
    ListAdapter<FilteredProductsResponse.FilteredProduct, ProductFilterListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductFilterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(private val binding: ProductFilterListItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(product: FilteredProductsResponse.FilteredProduct) {
            binding.apply {
                // Bind data to views
                product.imageurl?.let { ivProductBackground.loadImageWithCache(it, R.drawable.ic_bg_coffee) }
                product.producturl?.let { ivBottle.loadImageWithCache(it, R.drawable.ic_bottle) }
                tvDiscount.text = product.discountedprice
                tvOrginalPrice.text =
                    cutOnText(context, "CA$${product?.originalprice}")
                tvProductName.text = product.name
                tvProductDetails.text = product.description
                tvProductAddress.text = product.city.plus(" ${product.country}")
                ratingText.text = product.averagerating.toString()
                product.averagerating?.let {
                    ratingBar.rating = it.toFloat()
                }
                ratingsCount.text = product.totalratings
                product.userrating?.userimageurl?.let { ivRatingUser.loadImageWithCache(it, R.drawable.ic_male_placeholder) }
//                ivRatingUser.setImageResource(product.userImage)
                tvRatingUser.text = product.userrating?.username
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<FilteredProductsResponse.FilteredProduct>() {
        override fun areItemsTheSame(oldItem: FilteredProductsResponse.FilteredProduct, newItem: FilteredProductsResponse.FilteredProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FilteredProductsResponse.FilteredProduct, newItem: FilteredProductsResponse.FilteredProduct): Boolean {
            return oldItem == newItem
        }
    }
}