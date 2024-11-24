import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.data.remote.model.response.searchfragment.Country
import com.app.vivi.data.remote.model.response.searchfragment.Region
import com.app.vivi.databinding.ItemProductMakingCountriesBinding

class ShopByRegionAdapter :
    ListAdapter<Region, ShopByRegionAdapter.ShopByRegionAdapterViewHolder>(
        ShopByRegionAdapterDiffCallback()
    ) {

    // ViewHolder class
    class ShopByRegionAdapterViewHolder(val binding: ItemProductMakingCountriesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopByRegionAdapterViewHolder {
        val binding = ItemProductMakingCountriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopByRegionAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopByRegionAdapterViewHolder, position: Int) {
        val countryItem = getItem(position) // Use ListAdapter's `getItem()`
        with(holder.binding) {
            // Bind data to views
            imageView.setImageResource(R.drawable.ic_bg_coffee)
            textView.text = countryItem.name
        }
    }
}

class ShopByRegionAdapterDiffCallback : DiffUtil.ItemCallback<Region>() {
    override fun areItemsTheSame(
        oldItem: Region,
        newItem: Region
    ): Boolean {
        // Define the condition to check whether two items represent the same data
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Region,
        newItem: Region
    ): Boolean {
        // Define the condition to check whether two items have the same content
        return oldItem == newItem
    }
}
