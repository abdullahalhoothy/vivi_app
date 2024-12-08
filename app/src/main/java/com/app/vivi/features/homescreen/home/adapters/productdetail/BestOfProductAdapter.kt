package com.app.vivi.features.homescreen.home.adapters.productdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.response.products
import com.app.vivi.databinding.BestOfProductItemBinding

class BestOfProductAdapter(
    private val onItemClick: (item: products) -> Unit,
    private val onDiscountButtonClick: (item: products) -> Unit
) :
    ListAdapter<products, BestOfProductAdapter.ViewHolder>(
        PreferenceProductDiffCallback()
    ) {

    inner class ViewHolder(val binding: BestOfProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: products) {
            with(binding) {
                tvProductName.text = data.name
                tvProductDetails.text = data.description
                tvRatingText.text = data.ratingvalue.toString()
                ratingsCount.text = data.totalratings
                tvRatingDescription.text = data.ratingtext
                tvDiscount.text = "CA$${data.discountedprice}"


                tvDiscount.setOnClickListener {
                    onDiscountButtonClick(data)
                }
                // Handle item click
                root.setOnClickListener { onItemClick(data) }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            BestOfProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val data = getItem(position)

        holder.bind(getItem(position))
    }

    class PreferenceProductDiffCallback : DiffUtil.ItemCallback<products>() {
        override fun areItemsTheSame(oldItem: products, newItem: products): Boolean {
            // Compare the unique ID or some unique property of the item
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: products, newItem: products): Boolean {
            // Compare all the data inside the item
            return oldItem == newItem
        }
    }
}
