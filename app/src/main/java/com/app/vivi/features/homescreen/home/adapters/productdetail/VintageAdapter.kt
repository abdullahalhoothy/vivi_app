package com.app.vivi.features.homescreen.home.adapters.productdetail
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.data.remote.model.data.productdetailfragment.Vintage

class VintageAdapter : ListAdapter<Vintage, VintageAdapter.VintageViewHolder>(VintageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VintageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vintage, parent, false)
        return VintageViewHolder(view)
    }

    override fun onBindViewHolder(holder: VintageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VintageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val yearTextView: TextView = itemView.findViewById(R.id.txt_year)
        private val ratingTextView: TextView = itemView.findViewById(R.id.txt_rating)
        private val reviewsTextView: TextView = itemView.findViewById(R.id.txt_reviews)

        fun bind(vintage: Vintage) {
            yearTextView.text = vintage.year
            ratingTextView.text = vintage.rating
            reviewsTextView.text = vintage.reviews
        }
    }
}

class VintageDiffCallback : DiffUtil.ItemCallback<Vintage>() {
    override fun areItemsTheSame(oldItem: Vintage, newItem: Vintage) = oldItem.year == newItem.year
    override fun areContentsTheSame(oldItem: Vintage, newItem: Vintage) = oldItem == newItem
}