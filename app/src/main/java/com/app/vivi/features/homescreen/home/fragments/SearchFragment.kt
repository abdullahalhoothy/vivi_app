package com.app.vivi.features.homescreen.home.fragments

import ProductMakingCountriesAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentSearchBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.features.homescreen.home.adapters.search.OuterAdapter
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment :
    BaseFragmentVB<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()


    private val productMakingCountriesAdapter by lazy {
        ProductMakingCountriesAdapter()
    }

    private val outerAdapter by lazy {
        OuterAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapters()
        addObservers()
        getShopByTypes()
        getShopByCoffeeBeanTypes()
        getShopByCountries()

    }

    private fun initViews() {
        binding.apply {
            tvShopByType.text = getString(R.string.shop_by_type_txt, getString(R.string.app_name))
            tvShopByCountry.text =
                getString(R.string.shop_by_country_txt, getString(R.string.app_name))
            searchView.queryHint = getString(R.string.search_any_txt, getString(R.string.app_name))
        }

    }

    private fun initAdapters() {
        with(binding) {
            rvCountries.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = productMakingCountriesAdapter
            }

            rvOuter.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = outerAdapter
            }
        }

    }

    private fun addObservers() {
        collectWhenStarted {
            viewModel.shopByCoffeeBeanTypes.collectLatest { it ->
                it?.coffeeBeanTypes?.let { list ->
//                    productMakingCountriesAdapter.submitList(list)

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


    override fun getMyViewModel() = viewModel
}