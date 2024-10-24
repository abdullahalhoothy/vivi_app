package com.app.vivi.features.homescreen.home.fragments

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentProductBinding
import com.app.vivi.databinding.ProductItemBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.cutOnText
import com.app.vivi.features.homescreen.home.adapters.HoneyAdapter
import com.app.vivi.features.homescreen.home.adapters.ProductAdapter
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import com.app.vivi.helper.ImageGetter
import com.app.vivi.helper.cutOnText
import com.app.vivi.helper.ratingDescription
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

//        setupRatingDescriptions()
        initAdapters()
//        setupRecyclerViews()
        initListeners()
        addObservers()
        getRecommendedProducts()
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
        setupRecyclerViews()
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
            viewModel.recommendedProducts.collectLatest { product ->
                product?.bestPick?.let { bestPick ->
                    binding.inPickForYou.apply {
                        // Set the text values directly using safe calls and let
                        ratingText.text = bestPick.averagerating
                        ratingBar.rating = bestPick.averagerating?.toFloatOrNull() ?: 0f // Safely convert to float, defaulting to 0
                        ratingsCount.text = bestPick.totalratings
                        tvDiscount.text = bestPick.discountedprice
                        tvOrginalPrice.text = cutOnText(requireContext().applicationContext, bestPick.originalprice)
                        tvProductName.text = bestPick.productname
                    }
                } ?: run {
                    // Handle the case where bestPick is null, if needed
                    // For example, you can clear the views or set default values
                    clearViews(binding.inPickForYou)
                }

                product?.bestPick?.let { bestPick ->
                    binding.inJustForYou.apply {
                        // Set the text values directly using safe calls and let
                        ratingText.text = bestPick.averagerating
                        ratingBar.rating = bestPick.averagerating?.toFloatOrNull() ?: 0f // Safely convert to float, defaulting to 0
                        ratingsCount.text = bestPick.totalratings
                        tvDiscount.text = bestPick.discountedprice
                        tvOrginalPrice.text = cutOnText(requireContext().applicationContext, bestPick.originalprice)
                        tvProductName.text = bestPick.productname
                    }
                } ?: run {
                    // Handle the case where bestPick is null, if needed
                    // For example, you can clear the views or set default values
                    clearViews(binding.inJustForYou)
                }
            }
        }

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

    // Function to clear views if bestPick is null (optional)
    private fun clearViews(layout: ProductItemBinding) {
        layout.apply {
            ratingText.text = ""
            ratingBar.rating = 0f
            ratingsCount.text = ""
            tvDiscount.text = ""
            tvOrginalPrice.text = ""
            tvProductName.text = ""
        }
    }

    private fun getRecommendedProducts(){
        viewModel.getRecommendedProducts()
    }

    override fun getMyViewModel() = viewModel
}
