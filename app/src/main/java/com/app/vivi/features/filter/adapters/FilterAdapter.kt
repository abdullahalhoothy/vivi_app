package com.app.vivi.features.filter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.databinding.ItemFilterTagBinding

class FilterAdapter(
    private val items: List<String>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private var selectedItem: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterTagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    inner class FilterViewHolder(private val binding: ItemFilterTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, position: Int) {
            binding.tvFilter.text = item

            // Update UI based on selection
            if (position == selectedItem) {
                binding.root.isSelected = true
                binding.root.setBackgroundResource(R.drawable.unselected_tag_background) // Define in res/drawable
            } else {
                binding.root.isSelected = false
                binding.root.setBackgroundResource(R.drawable.selected_tag_background) // Define in res/drawable
            }

            binding.root.setOnClickListener {
                selectedItem = position
                notifyDataSetChanged()
                onItemClicked(item)
            }
        }
    }
}
