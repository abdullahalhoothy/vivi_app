package com.app.vivi.features.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentProductFilterListBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.features.filter.adapters.ProductFilterListAdapter
import com.app.vivi.features.homescreen.home.adapters.ProductOuterFavoriteAdapter
import com.app.vivi.features.homescreen.home.viewmodels.filter.ProductFilterListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductFilterListFragment : BaseFragmentVB<FragmentProductFilterListBinding>(FragmentProductFilterListBinding::inflate) {

    private val viewModel by viewModels<ProductFilterListViewModel>()

    private val mProductFilterListAdapter by lazy {
        ProductFilterListAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    }

    private fun navigateToProductDetailFragment(){
//        val action = HomeFragmentLatestDirections.actionLatestHomeFragmentToProductDetailFragment()
//        findNavController().navigateWithSingleTop(action)
    }

    private fun addObservers() {
        collectWhenStarted {
            viewModel.productFilterList.collectLatest {

                it?.let { productList ->
                    mProductFilterListAdapter.submitList(productList)

                }
            }
        }
    }

    override fun getMyViewModel() = viewModel
}
