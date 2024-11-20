package com.app.vivi.features.homescreen.home.adapters.productdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.data.productfragment.CharacteristicsData
import com.app.vivi.databinding.ItemCharacteristicsBinding

class CharacteristicsAdapter :
    ListAdapter<CharacteristicsData, CharacteristicsAdapter.SliderViewHolder>(SliderDiffCallback()) {

    // ViewHolder class to bind data
    inner class SliderViewHolder(private val binding: ItemCharacteristicsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(characteristicsData: CharacteristicsData) {
            with(binding) {
                lightLabel.text = characteristicsData.labelLight
                boldLabel.text = characteristicsData.labelBold

                // Set up the slider based on the setting option
                slider.value = characteristicsData.sliderValue
                slider.addOnChangeListener { _, value, _ ->
                    // Handle slider value change
                    characteristicsData.sliderValue = value
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemCharacteristicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // DiffUtil callback for calculating the differences between items
    class SliderDiffCallback : DiffUtil.ItemCallback<CharacteristicsData>() {
        override fun areItemsTheSame(oldItem: CharacteristicsData, newItem: CharacteristicsData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharacteristicsData, newItem: CharacteristicsData): Boolean {
            return oldItem == newItem
        }
    }
}
