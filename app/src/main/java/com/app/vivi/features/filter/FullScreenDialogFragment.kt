package com.app.vivi.features.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.databinding.DialogFullscreenBinding
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
        val filterItems = listOf("Red", "White", "Sparkling", "Rosé", "Dessert", "Natural")
        val filterAdapter = FilterAdapter(filterItems) { selectedItem ->
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
        val filterItems = listOf("United States", "Italy", "France", "Spain", "Argentina", "Show all")
        val filterAdapter = FilterAdapter(filterItems) { selectedItem ->
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
        val coffeeBeans = listOf(
            "Arabica",
            "Robusta",
            "Liberica",
            "Excelsa",
            "Geisha",
            "Blue Mountain",
            "Show all"
        )
        val filterAdapter = FilterAdapter(coffeeBeans) { selectedItem ->
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
        val coffeeRegions = listOf(
            "South America",
            "Central America",
            "Africa",
            "Asia-Pacific",
            "Middle East",
            "Caribbean",
            "Show all"
        )

        val filterAdapter = FilterAdapter(coffeeRegions) { selectedItem ->
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
