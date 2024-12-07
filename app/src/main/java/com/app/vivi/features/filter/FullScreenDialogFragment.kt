package com.app.vivi.features.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.app.vivi.data.remote.model.request.FilteredProductsRequest
import com.app.vivi.databinding.DialogFullscreenBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.features.filter.adapters.BeanTypeFilterAdapter
import com.app.vivi.features.filter.adapters.CountryFilterAdapter
import com.app.vivi.features.filter.adapters.FilterAdapter
import com.app.vivi.features.filter.adapters.RegionFilterAdapter
import com.app.vivi.features.homescreen.home.viewmodels.filter.ProductFilterListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FullScreenDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<ProductFilterListViewModel>()
    private var _binding: DialogFullscreenBinding? = null
    private val binding get() = _binding!! // Safe access to binding

    private var request: FilteredProductsRequest = FilteredProductsRequest()


    // Adapters initialized lazily
    private val typeFilterAdapter by lazy {
        FilterAdapter() { selectedItem ->
            println("Selected coffee item: $selectedItem")
        }
    }

    private val countryFilterAdapter by lazy {
        CountryFilterAdapter() { selectedItem ->
            println("Selected country item: $selectedItem")
        }
    }

    private val beanFilterAdapter by lazy {
        BeanTypeFilterAdapter() { selectedItem ->
            println("Selected bean item: $selectedItem")
        }
    }

    private val regionFilterAdapter by lazy {
        RegionFilterAdapter() { selectedItem ->
            println("Selected region item: $selectedItem")
        }
    }

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
        setupTypeFilterAdapter()
        setupCountryFilters()
        setupBeanFilters()
        setupRegionFilters()
        seekbar()
        rangeSliderSetup()
        getProductFiltersApi(request)
        addObservers()
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

    private fun setupTypeFilterAdapter() {
        binding.rvType.apply {
            layoutManager =  GridLayoutManager(requireContext(), 3)
            adapter = typeFilterAdapter
        }

        binding.rvType.adapter = typeFilterAdapter
    }

    private fun setupCountryFilters() {

        binding.rvCountry.apply {
            layoutManager =  GridLayoutManager(requireContext(), 2)
            adapter = countryFilterAdapter
        }

        binding.rvCountry.adapter = countryFilterAdapter
    }

    private fun setupBeanFilters() {
        binding.rvBean.apply {
            layoutManager =  GridLayoutManager(requireContext(), 2)
            adapter = beanFilterAdapter
        }

        binding.rvBean.adapter = beanFilterAdapter
    }

    private fun setupRegionFilters() {
        binding.rvRegion.apply {
            layoutManager =  GridLayoutManager(requireContext(), 2)
            adapter = regionFilterAdapter
        }

        binding.rvRegion.adapter = regionFilterAdapter
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


    private fun addObservers() {

        collectWhenStarted {
            viewModel.productFiltersResponse.collectLatest {

                it?.let { productList ->
                    typeFilterAdapter.submitList(productList.coffeeTypes)
                    beanFilterAdapter.submitList(productList.coffeeBeanTypes)
                    countryFilterAdapter.submitList(productList.countries)
                    regionFilterAdapter.submitList(productList.regions)

                }
            }
        }
    }

    private fun getProductFiltersApi(request: FilteredProductsRequest){
        viewModel.getProductFiltersApi(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
