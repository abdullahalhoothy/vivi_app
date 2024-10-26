package com.app.vivi.features.homescreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.response.Product
import com.app.vivi.databinding.HoneyProductListItemBinding

class HoneyAdapter :
    ListAdapter<Product, HoneyAdapter.HoneyViewHolder>(HoneyDiffCallback()) {

    inner class HoneyViewHolder(val binding: HoneyProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoneyViewHolder {
        val binding =
            HoneyProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoneyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoneyViewHolder, position: Int) {
        val data = getItem(position)

        with(holder.binding) {
            tvProductName.text = data.name
            tvProductDetails.text = data.description
            tvRatingText.text = data.ratingvalue.toString()
            ratingsCount.text = data.totalratings
            tvRatingDescription.text = data.ratingtext
            tvDiscount.text = "CA$${data.discountedprice}"

            // Load image using Glide with placeholders and error handling
            /*Glide.with(wineImage.context)
                .load(honeyData.imageUrl)
                .apply(RequestOptions().placeholder(R.drawable.placeholder_image).error(R.drawable.error_image))
                .into(wineImage)*/
        }
    }

    class HoneyDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            // Compare the unique ID or some unique property of the item
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            // Compare all the data inside the item
            return oldItem == newItem
        }
    }
}
