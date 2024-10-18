package com.app.honey.features.homescreen.home.fragments

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.honey.R
import com.app.honey.basefragment.BaseFragmentVB
import com.app.honey.data.remote.model.data.productfragment.HoneyData
import com.app.honey.data.remote.model.data.productfragment.Product
import com.app.honey.databinding.FragmentProductBinding
import com.app.honey.extension.collectWhenStarted
import com.app.honey.extension.cutOnText
import com.app.honey.features.homescreen.home.adapters.HoneyAdapter
import com.app.honey.features.homescreen.home.adapters.ProductAdapter
import com.app.honey.features.homescreen.home.viewmodels.ProductViewModel
import com.app.honey.helper.ImageGetter
import com.app.honey.helper.ratingDescription
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductFragment : BaseFragmentVB<FragmentProductBinding>(FragmentProductBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()
    private lateinit var mPickForYouAdapter: HoneyAdapter
    private lateinit var mYouMightInterestedAdapter: HoneyAdapter
    private lateinit var mProductAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRatingDescriptions()
        initAdapters()
        setupRecyclerViews()
        initListeners()
        addObservers()
    }

    private fun setupRatingDescriptions() {
        val ratingText = ratingDescription("Good solid Napa cab-vanilla quickly dissipates leaving the core of full dark fruit.", "3.5")
        val htmlContent = Html.fromHtml(ratingText, Html.FROM_HTML_MODE_LEGACY, ImageGetter(requireContext(), R.drawable.ic_star), null)

        with(binding) {
            inPickForYou.tvRatingDescription.text = htmlContent
            inJustForYou.tvRatingDescription.text = htmlContent
            inPickForYou.tvOrginalPrice.text = Html.fromHtml("CA$59.99".cutOnText(), Html.FROM_HTML_MODE_LEGACY, ImageGetter(requireContext()), null)
        }
    }

    private fun initAdapters() {
        mPickForYouAdapter = HoneyAdapter()
        mYouMightInterestedAdapter = HoneyAdapter()
        mProductAdapter = ProductAdapter()
    }

    private fun setupRecyclerViews() {
        with(binding) {
            rvPickForYou.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = mPickForYouAdapter
            }

            rvYouMightInterested.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = mYouMightInterestedAdapter
            }

            rvFavourite.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = mProductAdapter
            }
        }
    }

    private fun initListeners() {
        binding.inPickForYou.clProduct.setOnClickListener {
//            findNavController().navigate(R.id.action_latestHomeFragment_to_productDetailFragment)
            val action = HomeFragmentLatestDirections.actionLatestHomeFragmentToProductDetailFragment()

            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .build()

            findNavController().navigate(action, navOptions)

//            findNavController().navigate(HomeFragmentLatestDirections.actionLatestHomeFragmentToProductDetailFragment())
        }
    }

    private fun addObservers() {
        collectWhenStarted {
            viewModel.honeyList.collectLatest { honeyList ->
                mPickForYouAdapter.submitList(honeyList)
                mYouMightInterestedAdapter.submitList(honeyList) // Assuming you want the same list for both
            }
        }

        collectWhenStarted {
            viewModel.productList.collectLatest { productList ->
                mProductAdapter.submitList(productList)
            }
        }
    }

    override fun getMyViewModel() = viewModel
}
