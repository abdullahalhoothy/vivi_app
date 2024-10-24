package com.app.vivi.features.homescreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.data.productfragment.Review
import com.app.vivi.databinding.ReviewsItemBinding
import com.app.vivi.helper.createRatingDescription

class ReviewsAdapter :
    ListAdapter<Review, ReviewsAdapter.ReviewViewHolder>(ReviewDiffCallback()) {

    // ViewHolder class to bind data
    inner class ReviewViewHolder(private val binding: ReviewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            with(binding) {
//                val context = root.context
                val htmlContent = createRatingDescription(binding.root.context, review.reviewText, review.rating.toString())
//                val htmlContent = ratingDescription(review.reviewText, review.rating.toString())
                tvRating.text = htmlContent
                    /*Html.fromHtml(
                    htmlContent,
                    Html.FROM_HTML_MODE_LEGACY,
                    ImageGetter(context, R.drawable.ic_star),
                    null
                )*/
                tvRaterUsername.text = review.userName
                tvTime.text = review.time
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        // Inflate the view using ViewBinding
        val binding = ReviewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        // Bind the data to the ViewHolder
        holder.bind(getItem(position))
    }

    // Override getItemCount() to limit items displayed to 4
    override fun getItemCount(): Int {
        // Return the lesser of 4 or the actual number of items
        return minOf(super.getItemCount(), 4)
    }

    // DiffUtil callback for calculating the differences between items
    class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            // Here you can compare based on a unique ID if available
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            // Check whether the content is the same
            return oldItem == newItem
        }
    }
}
