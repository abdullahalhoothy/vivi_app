package com.app.vivi.features.filter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.data.remote.model.data.filter.TagData
import com.app.vivi.databinding.ItemFilterTagBinding
import com.app.vivi.extension.setCompoundDrawablesTint
import com.app.vivi.extension.setDrawableWithSize

class CountryFilterAdapter(
    private val items: List<TagData>,
    private val onItemClicked: (TagData) -> Unit
) : RecyclerView.Adapter<CountryFilterAdapter.FilterViewHolder>() {

    // Method to unselect all items
    fun unselectAll() {
        items.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterTagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilterViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    inner class FilterViewHolder(private val context: Context, private val binding: ItemFilterTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: TagData, position: Int) {
            binding.tvFilter.text = tag.name
            binding.tvFilter.setTextColor( ContextCompat.getColor(
                binding.root.context,
                if (tag.isSelected) R.color.colorWhite else R.color.colorBlack
            ))


            if (tag.isSelected) {
                binding.flMain.isSelected = true
                binding.flMain.setBackgroundResource(R.drawable.unselected_tag_background) // Define this in res/drawable
            } else {
                binding.flMain.isSelected = false
                binding.flMain.setBackgroundResource(R.drawable.selected_tag_background) // Define in res/drawable
            }

           /* val tintColor = ContextCompat.getColor(
                context,
                if (tag.isSelected) R.color.colorWhite else R.color.colorBlack
            )
            binding.tvFilter.setDrawableWithSize(context, tag.icon, 50, 50, 5, tintColor) // Set the icon
*/

            binding.root.setOnClickListener {
                tag.isSelected = !tag.isSelected  // Toggle the selection state
                notifyItemChanged(position) // Update the clicked item
                onItemClicked(tag) // Callback to notify about the click
            }
        }
    }
}
