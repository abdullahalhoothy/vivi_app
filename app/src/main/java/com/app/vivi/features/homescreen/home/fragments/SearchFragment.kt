package com.app.vivi.features.homescreen.home.fragments

import ProductMakingCountriesAdapter
import ShopByRegionAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentSearchBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.navigateWithSingleTop
import com.app.vivi.extension.setClickListener
import com.app.vivi.features.homescreen.home.adapters.search.OuterAdapter
import com.app.vivi.features.homescreen.home.adapters.search.OuterShopByBeanAdapter
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment :
    BaseFragmentVB<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()


    private val productMakingCountriesAdapter by lazy {
        ProductMakingCountriesAdapter(onItemClick = {
            navigateToProductFilterListFragment()
        })
    }

    private val shopByRegionsAdapter by lazy {
        ShopByRegionAdapter(onItemClick = {
            navigateToProductFilterListFragment()
        })
    }

    private val outerAdapter by lazy {
        OuterAdapter(onItemClick = {
            navigateToProductFilterListFragment()
        })
    }

    private val outerShopByBeanAdapter by lazy {
        OuterShopByBeanAdapter(onItemClick = {
            navigateToProductFilterListFragment()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        initAdapters()
        addObservers()
        getShopByTypes()
        getShopByCoffeeBeanTypes()
        getShopByCountries()
        getShopByRegions()

    }

    private fun initViews() {
        binding.apply {
            tvShopByType.text = getString(R.string.shop_by_type_txt, getString(R.string.app_name))
            tvShopByBeanType.text = getString(R.string.shop_by_bean_txt, getString(R.string.app_name))
            tvShopByRegion.text = getString(R.string.shop_by_region_txt, getString(R.string.app_name))
            tvShopByCountry.text =
                getString(R.string.shop_by_country_txt, getString(R.string.app_name))
            searchView.queryHint = getString(R.string.search_any_txt, getString(R.string.app_name))
        }
    }

    private fun initListeners(){
        with(binding){

            //Extension same clicklistener function for multiple views
            setClickListener(tvShopByType, icShopByTypeForward){
                navigateToProductFiltersFragment()
            }

            //Extension same clicklistener function for multiple views
            setClickListener(tvShopByCountry, icShopByCountryForward){
                navigateToProductFiltersFragment()
            }

            //Extension same clicklistener function for multiple views
            setClickListener(tvShopByBeanType, icShopByBeanTypeForward){
                navigateToProductFiltersFragment()
            }

            //Extension same clicklistener function for multiple views
            setClickListener(tvShopByRegion, icShopByRegionForward){
                navigateToProductFiltersFragment()
            }
        }
    }

    private fun initAdapters() {
        with(binding) {
            rvCountries.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = productMakingCountriesAdapter
            }

            rvRegion.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = shopByRegionsAdapter
            }

            rvOuter.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = outerAdapter
            }

            rvOuterShopByBeanType.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = outerShopByBeanAdapter
            }
        }

    }

    private fun navigateToProductFilterListFragment(){
        val action =
            HomeFragmentLatestDirections.actionLatestHomeFragmentToProductFilterListFragment()
        findNavController().navigateWithSingleTop(action)
    }

    private fun navigateToProductFiltersFragment(){
        val action =
            HomeFragmentLatestDirections.actionLatestHomeFragmentToProductFiltersFragment()
        findNavController().navigateWithSingleTop(action)
    }

    private fun addObservers() {
        collectWhenStarted {
            viewModel.shopByCoffeeBeanTypes.collectLatest { it ->
                it?.let { list ->
                    outerShopByBeanAdapter.submitList(list)

                }
            }
        }

        collectWhenStarted {
            viewModel.shopByCountries.collectLatest { it ->
                it?.countries?.let { list ->
                    productMakingCountriesAdapter.submitList(list)
                }
            }
        }

        collectWhenStarted {
            viewModel.shopByType.collectLatest { list ->
                list?.let { outerAdapter.submitList(it) }
            }
        }

        collectWhenStarted {
            viewModel.shopByRegion.collectLatest { list ->
                list?.shopByRegion.let {
                    shopByRegionsAdapter.submitList(it)

                }
            }
        }
    }

    private fun getShopByTypes(){
        viewModel.getShopByCoffeeTypes()
    }

    private fun getShopByCoffeeBeanTypes(){
        viewModel.getShopByCoffeeBeanTypes()
    }

    private fun getShopByCountries(){
        viewModel.getShopByCountries()
    }

    private fun getShopByRegions(){
        viewModel.getShopByRegions()
    }


    override fun getMyViewModel() = viewModel
}