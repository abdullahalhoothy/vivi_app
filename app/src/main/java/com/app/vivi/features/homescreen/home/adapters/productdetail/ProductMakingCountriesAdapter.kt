import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.data.remote.model.data.productdetailfragment.ProductMakingCountries
import com.app.vivi.databinding.ItemProductMakingCountriesBinding

class ProductMakingCountriesAdapter :
    ListAdapter<ProductMakingCountries, ProductMakingCountriesAdapter.ProductMakingCountriesViewHolder>(
        ProductMakingCountriesDiffCallback()
    ) {

    // ViewHolder class
    class ProductMakingCountriesViewHolder(val binding: ItemProductMakingCountriesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductMakingCountriesViewHolder {
        val binding = ItemProductMakingCountriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductMakingCountriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductMakingCountriesViewHolder, position: Int) {
        val countryItem = getItem(position) // Use ListAdapter's `getItem()`
        with(holder.binding) {
            // Bind data to views
            imageView.setImageResource(countryItem.imageRes)
            textView.text = countryItem.country
        }
    }
}

class ProductMakingCountriesDiffCallback : DiffUtil.ItemCallback<ProductMakingCountries>() {
    override fun areItemsTheSame(
        oldItem: ProductMakingCountries,
        newItem: ProductMakingCountries
    ): Boolean {
        // Define the condition to check whether two items represent the same data
        return oldItem.country == newItem.country
    }

    override fun areContentsTheSame(
        oldItem: ProductMakingCountries,
        newItem: ProductMakingCountries
    ): Boolean {
        // Define the condition to check whether two items have the same content
        return oldItem == newItem
    }
}
