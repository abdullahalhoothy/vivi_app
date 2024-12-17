package com.app.vivi.features.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.app.vivi.basefragment.BaseDialogFragmentFullscreen
import com.app.vivi.baseviewmodel.BaseViewModel
import com.app.vivi.data.remote.model.request.FilteredProductsRequest
import com.app.vivi.databinding.DialogFullscreenBinding
import com.app.vivi.features.filter.adapters.BeanTypeFilterAdapter
import com.app.vivi.features.filter.adapters.CountryFilterAdapter
import com.app.vivi.features.filter.adapters.FilterAdapter
import com.app.vivi.features.filter.adapters.RegionFilterAdapter
import com.app.vivi.features.filter.adapters.SizesFilterAdapter
import com.app.vivi.features.filter.adapters.StylesFilterAdapter
import com.app.vivi.features.homescreen.home.viewmodels.filter.ProductFilterListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FullScreenDialogFragment : BaseDialogFragmentFullscreen<DialogFullscreenBinding>(
    DialogFullscreenBinding::inflate
) {

    private val viewModel by viewModels<ProductFilterListViewModel>()
    override fun getMyViewModel(): BaseViewModel {
        return viewModel
    }

    private var debounceJob: Job? = null // Track the debounce job

    private var request: FilteredProductsRequest = FilteredProductsRequest()
    val typeIds: MutableList<Int> = mutableListOf()
    val countryNames: MutableList<String> = mutableListOf()
    val rawMaterialIds: MutableList<Int> = mutableListOf()
    val regionIds: MutableList<Int> = mutableListOf()
    val stylesIds: MutableList<Int> = mutableListOf()
    val sizesIds: MutableList<Int> = mutableListOf()

    // Adapters initialized lazily
    private val typeFilterAdapter by lazy {
        FilterAdapter() { selectedItem ->
            println("Selected coffee item: $selectedItem")
            // Add or remove region ID based on selection state
            selectedItem.id?.let { id ->
                if (selectedItem.isSelected) typeIds.add(id) else typeIds.remove(id)
            }

            // Update the request object with the current region IDs and trigger API request
            request.typeIds = typeIds
            getProductFiltersApi(request)
        }
    }

    private val countryFilterAdapter by lazy {
        CountryFilterAdapter() { selectedItem ->
            println("Selected country item: $selectedItem")
            // Add or remove region ID based on selection state
            selectedItem.name?.let { name ->
                if (selectedItem.isSelected) countryNames.add(name) else countryNames.remove(name)
            }

            // Update the request object with the current region IDs and trigger API request
            request.countryNames = countryNames
            getProductFiltersApi(request)
        }
    }

    private val beanFilterAdapter by lazy {
        BeanTypeFilterAdapter() { selectedItem ->
            println("Selected bean item: $selectedItem")
            // Add or remove region ID based on selection state
            selectedItem.id?.let { id ->
                if (selectedItem.isSelected) rawMaterialIds.add(id) else rawMaterialIds.remove(id)
            }

            // Update the request object with the current region IDs and trigger API request

            request.rawMaterialIds = rawMaterialIds
            getProductFiltersApi(request)
        }
    }

    private val regionFilterAdapter by lazy {
        RegionFilterAdapter { selectedItem ->
            println("Selected region item: $selectedItem")

            // Add or remove region ID based on selection state
            selectedItem.id?.let { id ->
                if (selectedItem.isSelected) regionIds.add(id) else regionIds.remove(id)
            }

            // Update the request object with the current region IDs and trigger API request

            request.regionIds = regionIds
            getProductFiltersApi(request)
        }
    }

    private val stylesFilterAdapter by lazy {
        StylesFilterAdapter { selectedItem ->
            println("Selected styles item: $selectedItem")

            // Add or remove region ID based on selection state
            selectedItem.id?.let { id ->
                if (selectedItem.isSelected) stylesIds.add(id) else stylesIds.remove(id)
            }

            // Update the request object with the current region IDs and trigger API request

            request.styleIds = stylesIds
            getProductFiltersApi(request)
        }
    }

    private val sizesFilterAdapter by lazy {
        SizesFilterAdapter { selectedItem ->
            println("Selected styles item: $selectedItem")

            // Add or remove region ID based on selection state
            selectedItem.id?.let { id ->
                if (selectedItem.isSelected) sizesIds.add(id) else sizesIds.remove(id)
            }

            // Update the request object with the current region IDs and trigger API request

            request.sizeIds = sizesIds
            getProductFiltersApi(request)
        }
    }


    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using ViewBinding
//        _binding = DialogFullscreenBinding.inflate(inflater, container, false)
        return binding.root
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        seekbar()
        rangeSliderSetup()
        getProductFiltersApi(request)
        addObservers()
    }

    override fun onStart() {
        super.onStart()

        // Make the BottomSheetDialog full-screen
        val dialog = dialog ?: return
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true // Skip collapsed state
        behavior.isFitToContents = true // Makes it fit the content
    }

    private fun setupAdapters() {
        binding.rvType.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = typeFilterAdapter
        }

        binding.rvCountry.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = countryFilterAdapter
        }

        binding.rvBean.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = beanFilterAdapter
        }

        binding.rvRegion.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = regionFilterAdapter
        }

        binding.rvStyles.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = stylesFilterAdapter
        }

        binding.rvSizes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = sizesFilterAdapter
        }

    }

    private fun seekbar() {
        binding.seekbarRating.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Convert progress to a rating value (e.g., min 3.0, max 5.0)
                val minRating = 0.0 + (progress / 100.0) * 5.0
                binding.tvMinRating.text = "Min. ★%.1f".format(minRating)

                // Cancel the previous job if user continues to slide
                debounceJob?.cancel()

                // Start a new coroutine with delay for debouncing
                debounceJob = lifecycleScope.launch {
                    delay(500) // Wait for 500ms after the user stops
                    request.minRatingValue = "%.1f".format(minRating)
                    getProductFiltersApi(request)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optional: Do something when user starts dragging
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: Do something when user stops dragging
            }
        })
    }

    private fun rangeSliderSetup() {
        // Set initial values for the two thumbs
        val values = listOf(100f, 250f)
        binding.rangeSliderPrice.values = values

        // Add a listener for changes
        binding.rangeSliderPrice.addOnChangeListener { slider, value, fromUser ->
            val thumb1Value = slider.values[0]
            val thumb2Value = slider.values[1]
            binding.tvRangeMinPrice.text = "CA$${thumb1Value} - CA${thumb2Value}"

            // Cancel the previous job if user continues to slide
            debounceJob?.cancel()

            // Start a new coroutine with delay for debouncing
            debounceJob = lifecycleScope.launch {
                delay(500) // Wait for 500ms after the user stops
                request.minPrice = thumb1Value.toString()
                request.maxPrice = thumb2Value.toString()
                getProductFiltersApi(request)
                Log.d("RangeSlider", "API called: Min: $thumb1Value, Max: $thumb2Value")
            }
        }
    }


    private fun addObservers() {

        lifecycleScope.launch {
            viewModel.productFiltersResponse.collectLatest {
                it?.let { productList ->
                    typeFilterAdapter.submitList(productList.coffeeData.coffeeTypes)
                    beanFilterAdapter.submitList(productList.coffeeData.coffeeBeanTypes)
                    countryFilterAdapter.submitList(productList.coffeeData.countries)
                    regionFilterAdapter.submitList(productList.coffeeData.regions)
                    stylesFilterAdapter.submitList(productList.coffeeData.coffeeStyles)
                    sizesFilterAdapter.submitList(productList.coffeeData.sizes)

                } ?: run {
                }
            }
        }
    }

    private fun getProductFiltersApi(request: FilteredProductsRequest) {
        viewModel.getProductFiltersApi(request)
    }

}
