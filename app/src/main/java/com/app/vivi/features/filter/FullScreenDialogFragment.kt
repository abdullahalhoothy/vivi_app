package com.app.vivi.features.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.R
import com.app.vivi.data.remote.model.data.filter.TagData
import com.app.vivi.databinding.DialogFullscreenBinding
import com.app.vivi.features.filter.adapters.CountryFilterAdapter
import com.app.vivi.features.filter.adapters.FilterAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FullScreenDialogFragment : BottomSheetDialogFragment() {

    private var _binding: DialogFullscreenBinding? = null
    private val binding get() = _binding!! // Safe access to binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using ViewBinding
        _binding = DialogFullscreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up dynamic filters or other views
        setupDynamicFilters()
        setupCountryFilters()
        setupBeanFilters()
        setupRegionFilters()
        seekbar()
        rangeSliderSetup()
    }

    override fun onStart() {
        super.onStart()

        // Make the BottomSheetDialog full-screen
        val dialog = dialog ?: return
        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true // Skip collapsed state
        behavior.isFitToContents = true // Makes it fit the content
    }

    private fun setupDynamicFilters() {
        // Example: Populate RecyclerView with dynamic filters
        // For now, let's set up a static example

        val coffeeItems = listOf(
            TagData("Espresso", R.drawable.ic_coffee_cup_1), // Icon for Espresso
            TagData("Americano", R.drawable.ic_coffee_cup_1), // Icon for Americano
            TagData("Latte", R.drawable.ic_coffee_cup_1), // Icon for Latte
            TagData("Cappuccino", R.drawable.ic_coffee_cup_1), // Icon for Cappuccino
            TagData("Mocha", R.drawable.ic_coffee_cup_1), // Icon for Mocha
            TagData("Flat White", R.drawable.ic_coffee_cup), // Icon for Flat White
            TagData("Macchiato", R.drawable.ic_coffee_cup), // Icon for Macchiato
            TagData("Macchiato", R.drawable.ic_coffee_cup), // Icon for Macchiato
            TagData("Cold Brew", R.drawable.ic_coffee_cup), // Icon for Cold Brew
            TagData("Iced Coffee", R.drawable.ic_coffee_cup) // Icon for Iced Coffee
        )


//        val filterItems = listOf("Red", "c", "Sparkling", "Rosé", "Dessert", "Natural")
        val filterAdapter = FilterAdapter(coffeeItems) { selectedItem ->
            // Handle item click
            println("Selected item: $selectedItem")
        }
        binding.rvType.apply {
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager =  GridLayoutManager(requireContext(), 3)
            adapter = filterAdapter
        }

        binding.rvType.adapter = filterAdapter
    }

    private fun setupCountryFilters() {
        // Example: Populate RecyclerView with dynamic filters
        // For now, let's set up a static example
//        val filterItems = listOf("United States", "Italy", "France", "Spain", "Argentina", "Show all")
        val countryItems = listOf(
            TagData("Brazil", R.drawable.ic_uk_globe),  // Icon for Brazil
            TagData("Colombia", R.drawable.ic_uk_globe), // Icon for Colombia
            TagData("Vietnam", R.drawable.ic_uk_globe), // Icon for Vietnam
            TagData("Ethiopia", R.drawable.ic_uk_globe), // Icon for Ethiopia
            TagData("Indonesia", R.drawable.ic_uk_globe), // Icon for Indonesia
            TagData("Mexico", R.drawable.ic_uk_globe), // Icon for Mexico
        )

        val filterAdapter = CountryFilterAdapter(countryItems) { selectedItem ->
            // Handle item click
            println("Selected item: $selectedItem")
        }
        binding.rvCountry.apply {
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager =  GridLayoutManager(requireContext(), 2)
            adapter = filterAdapter
        }

        binding.rvCountry.adapter = filterAdapter
    }

    private fun setupBeanFilters() {
        // Example: Populate RecyclerView with dynamic filters
        // For now, let's set up a static example
        val coffeeBeansItems = listOf(
            TagData("Arabica", R.drawable.ic_uk_globe),  // Icon for Arabica
            TagData("Robusta", R.drawable.ic_uk_globe), // Icon for Robusta
            TagData("Liberica", R.drawable.ic_uk_globe), // Icon for Liberica
            TagData("Excelsa", R.drawable.ic_uk_globe), // Icon for Excelsa
            TagData("Bourbon", R.drawable.ic_uk_globe), // Icon for Bourbon
            TagData("Caturra", R.drawable.ic_uk_globe), // Icon for Caturra
            TagData("Gesha", R.drawable.ic_uk_globe), // Icon for Gesha
            TagData("Pacamara", R.drawable.ic_uk_globe), // Icon for Pacamara
            TagData("SL28", R.drawable.ic_uk_globe), // Icon for SL28
            TagData("TYPICA", R.drawable.ic_uk_globe) // Icon for TYPICA
        )

        val filterAdapter = CountryFilterAdapter(coffeeBeansItems) { selectedItem ->
            // Handle item click
            println("Selected item: $selectedItem")
        }
        binding.rvBean.apply {
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager =  GridLayoutManager(requireContext(), 2)
            adapter = filterAdapter
        }

        binding.rvBean.adapter = filterAdapter
    }

    private fun setupRegionFilters() {
        val regionItems = listOf(
            TagData("Central America", R.drawable.ic_uk_globe),  // Icon for Central America
            TagData("South America", R.drawable.ic_uk_globe), // Icon for South America
            TagData("East Africa", R.drawable.ic_uk_globe), // Icon for East Africa
            TagData("Southeast Asia", R.drawable.ic_uk_globe), // Icon for Southeast Asia
            TagData("Caribbean", R.drawable.ic_uk_globe), // Icon for Caribbean
            TagData("Pacific Islands", R.drawable.ic_uk_globe), // Icon for Pacific Islands
        )


        val filterAdapter = CountryFilterAdapter(regionItems) { selectedItem ->
            // Handle item click
            println("Selected item: $selectedItem")
        }
        binding.rvRegion.apply {
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager =  GridLayoutManager(requireContext(), 2)
            adapter = filterAdapter
        }

        binding.rvRegion.adapter = filterAdapter
    }

    private fun seekbar(){
        binding.seekbarRating.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Convert progress to a rating value (e.g., min 3.0, max 5.0)
                val minRating = 0.0 + (progress / 100.0) * 5.0
                binding.tvMinRating.text = "Min. ★%.1f".format(minRating)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optional: Do something when user starts dragging
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: Do something when user stops dragging
            }
        })
    }

    private fun rangeSliderSetup(){
        // Set initial values for the two thumbs
        val values = listOf(100f, 250f)
        binding.rangeSliderPrice.values = values

// Add a listener for changes
        binding.rangeSliderPrice.addOnChangeListener { slider, value, fromUser ->
            val thumb1Value = slider.values[0]
            val thumb2Value = slider.values[1]
            binding.tvRangeMinPrice.text = "CA$${thumb1Value} - CA${thumb2Value}"
            Log.d("RangeSlider", "Thumb 1: $thumb1Value, Thumb 2: $thumb2Value")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
