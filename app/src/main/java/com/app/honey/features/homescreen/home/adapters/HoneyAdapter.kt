package com.app.honey.features.homescreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.honey.data.remote.model.data.productfragment.HoneyData
import com.app.honey.databinding.HoneyProductListItemBinding

class HoneyAdapter :
    ListAdapter<HoneyData, HoneyAdapter.HoneyViewHolder>(HoneyDiffCallback()) {

    inner class HoneyViewHolder(val binding: HoneyProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoneyViewHolder {
        val binding =
            HoneyProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoneyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoneyViewHolder, position: Int) {
        val honeyData = getItem(position)

        with(holder.binding) {
            tvProductName.text = honeyData.name
            tvProductDetails.text = honeyData.address
            tvRatingText.text = honeyData.ratingValue.toString()
            ratingsCount.text = honeyData.totalRatings
            tvRatingDescription.text = honeyData.ratingDescription
            tvDiscount.text = "CA$${honeyData.discount}"

            // Load image using Glide with placeholders and error handling
            /*Glide.with(wineImage.context)
                .load(honeyData.imageUrl)
                .apply(RequestOptions().placeholder(R.drawable.placeholder_image).error(R.drawable.error_image))
                .into(wineImage)*/
        }
    }

    class HoneyDiffCallback : DiffUtil.ItemCallback<HoneyData>() {
        override fun areItemsTheSame(oldItem: HoneyData, newItem: HoneyData): Boolean {
            // Compare the unique ID or some unique property of the item
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HoneyData, newItem: HoneyData): Boolean {
            // Compare all the data inside the item
            return oldItem == newItem
        }
    }
}
