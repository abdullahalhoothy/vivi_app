package com.app.vivi.features.homescreen.home.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.data.remote.model.response.Product
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
import kotlin.math.log

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
        handleOnScrollChangeListener()
        initListeners()
        addObservers()
        getRecommendedProducts()
    }



    private fun handleOnScrollChangeListener() {
        var isApiCalled = false
        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val buttonLocation = IntArray(2)
            binding.rvPickForYou.getLocationOnScreen(buttonLocation)

            // Check if scrolling upwards and button is now visible
            if (scrollY < oldScrollY && buttonLocation[1] > 0) {
                Log.d("Scrolling: Visible", " srollY: $scrollY, oldScrollY: $oldScrollY, buttonLocation: ${buttonLocation[1]}")

                // Only call the API once when scrolling up and view becomes visible
                if (!isApiCalled) {
                    Log.d("Scrolling: ", " isApiCalled: $isApiCalled")
                    getPreferenceProductDetail()
                    isApiCalled = true  // Set flag to prevent further calls
                }
            }
        }
        /*binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val buttonLocation = IntArray(2)
            binding.rvPickForYou.getLocationOnScreen(buttonLocation)

            // Check if we are scrolling down, and the button has moved out of view, then back into view
            if (scrollY > oldScrollY && buttonLocation[1] < 0) {
                // Button is scrolled out of view, show the fixed button
                binding.rvPickForYou.visibility = View.VISIBLE
                Log.d("Scrolling: Visible", " srollY: $scrollY, oldScrollY: $oldScrollY, buttonLocation: ${buttonLocation[1]}")

                // Only call the API once when the button first becomes visible on scroll down
                if (!isApiCalled) {
                    Log.d("Scrolling: ", " isApiCalled: $isApiCalled")

                    getPreferenceProductDetail()
                    isApiCalled = true  // Set flag to true to prevent further calls
                }
            } else if (scrollY < oldScrollY && buttonLocation[1] > 0) {
                // Button is still in view, hide the fixed button
                Log.d("Scrolling: Hide", " srollY: $scrollY, oldScrollY: $oldScrollY, buttonLocation: ${buttonLocation[1]}")
//                binding.rvPickForYou.visibility = View.GONE
            }
        }*/
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
            viewModel.preferenceProductDetail.collectLatest {

                it?.product?.let { product->
                    val list = listOf(product)
                    mPickForYouAdapter.submitList(list)
                    mYouMightInterestedAdapter.submitList(list)

                    product?.userrating?.let {

                    }
                }
            }
        }

        collectWhenStarted {
            viewModel.honeyList.collectLatest { honeyList ->
//                mPickForYouAdapter.submitList(honeyList)
//                mYouMightInterestedAdapter.submitList(honeyList) // Assuming you want the same list for both
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

    private fun getPreferenceProductDetail(){
        viewModel.getPreferenceProductDetail()
    }

    override fun getMyViewModel() = viewModel
}
