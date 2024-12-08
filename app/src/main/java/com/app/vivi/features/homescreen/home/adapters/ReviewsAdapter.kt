package com.app.vivi.features.homescreen.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.data.remote.model.response.Review
import com.app.vivi.databinding.ReviewsItemBinding
import com.app.vivi.extension.setDrawableWithSize
import com.app.vivi.helper.createRatingDescription

class ReviewsAdapter(private val onCommentClick: (Item: Review) -> Unit) :
    ListAdapter<Review, ReviewsAdapter.ReviewViewHolder>(ReviewDiffCallback()) {

    // ViewHolder class to bind data
    inner class ReviewViewHolder(private val binding: ReviewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            with(binding) {
                val htmlContent = createRatingDescription(binding.root.context, review.description, review.averageRating.toString())
                tvRating.text = htmlContent
                tvRaterUsername.text = "${review.name} (${review.totalRatings} " +
                        "${root.context.getString(R.string.ratings_txt)})"

                tvComment.setDrawableWithSize(binding.root.context,
                    R.drawable.ic_comment,
                    50, 50,
                    binding.root.context.resources.getDimensionPixelSize(R.dimen.sdp_10))

                tvTime.text = review.time

                tvComment.setOnClickListener {
                    onCommentClick(review)
                }
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
