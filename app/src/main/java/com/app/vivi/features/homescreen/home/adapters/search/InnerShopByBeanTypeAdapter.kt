package com.app.vivi.features.homescreen.home.adapters.search

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.response.searchfragment.CoffeeBeanType
import com.app.vivi.data.remote.model.response.searchfragment.ProductType
import com.app.vivi.databinding.ItemInnerRecyclerviewBinding
import com.app.vivi.databinding.ShopByBeanItemInnerRecyclerviewBinding
import roundLeftCorners

class InnerShopByBeanTypeAdapter : RecyclerView.Adapter<InnerShopByBeanTypeAdapter.InnerViewHolder>() {

    private var items: List<CoffeeBeanType> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ShopByBeanItemInnerRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // Update the list with DiffUtil
    fun submitList(newItems: List<CoffeeBeanType>) {
        val diffCallback = InnerDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class InnerViewHolder(private val binding: ShopByBeanItemInnerRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:CoffeeBeanType) {
            binding.textView.text = item.name

// Set the background tint using a hexadecimal color code
            val color = Color.parseColor(item.colorCode)  // Hexadecimal color code
            val originalColor = item.colorCode  // Example hex code
            val darkerColor = darkenColor(originalColor, 0.2f)
            ViewCompat.setBackgroundTintList(binding.clMain, ColorStateList.valueOf(color))
//            binding.clMain.setBackgroundTint(Color.parseColor(item.colorCode))

            binding.imageView.setBackgroundColor(darkerColor)
            binding.imageView.roundLeftCorners(20f)
            // Optionally, set imageView's image source based on item
        }
    }
    fun darkenColor(color: String?, factor: Float): Int {
        // Parse the hexadecimal color code
        val parsedColor = Color.parseColor(color)

        // Extract the RGB components from the color
        val alpha = Color.alpha(parsedColor)
        val red = Color.red(parsedColor)
        val green = Color.green(parsedColor)
        val blue = Color.blue(parsedColor)

        // Apply the factor to darken the color (e.g., 0.8 makes it 20% darker)
        val darkenedRed = (red * factor).toInt()
        val darkenedGreen = (green * factor).toInt()
        val darkenedBlue = (blue * factor).toInt()

        // Ensure the color components are within the valid range (0-255)
        return Color.argb(alpha, darkenedRed.coerceIn(0, 255), darkenedGreen.coerceIn(0, 255), darkenedBlue.coerceIn(0, 255))
    }

    // DiffUtil Callback for calculating item differences
    class InnerDiffCallback(
        private val oldList: List<CoffeeBeanType>,
        private val newList: List<CoffeeBeanType>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Assuming items have unique identifiers, here items are compared by content
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Additional checks for content equality, if needed
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
