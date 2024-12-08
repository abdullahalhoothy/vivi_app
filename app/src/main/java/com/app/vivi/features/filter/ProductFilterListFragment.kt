package com.app.vivi.features.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.data.remote.model.request.FilteredProductsRequest
import com.app.vivi.databinding.FragmentProductFilterListBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.features.filter.adapters.ProductFilterListAdapter
import com.app.vivi.features.homescreen.home.viewmodels.filter.ProductFilterListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductFilterListFragment :
    BaseFragmentVB<FragmentProductFilterListBinding>(FragmentProductFilterListBinding::inflate) {

    private val viewModel by viewModels<ProductFilterListViewModel>()

    private val mProductFilterListAdapter by lazy {
        ProductFilterListAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFilteredProductsApi(FilteredProductsRequest(minRatingValue = "2.1"))
        initViews()
        initAdapters()
        initListeners()
        addObservers()
    }


    private fun initViews() {
        binding.apply {

        }

    }

    private fun initAdapters() {
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        with(binding) {

            rvProductFilterList.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = mProductFilterListAdapter
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            flFilter.setOnClickListener { navigateToFullScreenDialogFragment() }
        }
    }

    private fun navigateToFullScreenDialogFragment() {
        val dialog = FullScreenDialogFragment()
        dialog.show(parentFragmentManager, "FullScreenDialog")
    }

    private fun addObservers() {
        collectWhenStarted {
            viewModel.productFilterList.collectLatest {

                it?.let { productList ->
//                    mProductFilterListAdapter.submitList(productList)

                }
            }
        }

        collectWhenStarted {
            viewModel.filteredProductList.collectLatest {

                it?.let { productList ->
                    mProductFilterListAdapter.submitList(productList)

                }
            }
        }
    }

    private fun getFilteredProductsApi(request: FilteredProductsRequest){
        viewModel.getFilteredProductsApi(request)
    }


    override fun getMyViewModel() = viewModel
}
