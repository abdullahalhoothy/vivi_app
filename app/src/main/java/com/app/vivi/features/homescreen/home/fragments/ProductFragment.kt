package com.app.vivi.features.homescreen.home.fragments

import RecommendedProductData
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
import com.app.vivi.databinding.FragmentProductBinding
import com.app.vivi.databinding.ProductItemBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.cutOnText
import com.app.vivi.features.homescreen.home.adapters.PreferenceProductAdapter
import com.app.vivi.features.homescreen.home.adapters.ProductAdapter
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import com.app.vivi.helper.ImageGetter
import com.app.vivi.helper.createRatingDescription
import com.app.vivi.helper.cutOnText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductFragment : BaseFragmentVB<FragmentProductBinding>(FragmentProductBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()
    private lateinit var mPickForYouAdapter: PreferenceProductAdapter
    private lateinit var mYouMightInterestedAdapter: PreferenceProductAdapter
    private lateinit var mProductAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapters()
        handleOnScrollChangeListener()
        initListeners()
        addObservers()
        getRecommendedProducts()
    }


    private fun initViews() {
        binding.apply {
            inRating.tvBaseOnYourActivity.text =
                getString(R.string.based_on_your_activity_txt, getString(R.string.app_name))
            inRating.tvTitle.text =
                getString(R.string.do_you_like_txt, getString(R.string.app_name))

            inJustForYou.tvRecommendation.text =
                getString(
                    R.string.based_on_your_taste_and_the_magic_of_deep_knowledge_txt,
                    getString(R.string.app_name)
                )
            inPickForYou.tvRecommendation.text =
                getString(
                    R.string.based_on_your_taste_and_the_magic_of_deep_knowledge_txt,
                    getString(R.string.app_name)
                )

            tvFavouriteTitle.text =
                getString(R.string.find_your_new_favorite_txt, getString(R.string.app_name))
        }

    }

    private fun handleOnScrollChangeListener() {
        var isApiCalled = false
        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val buttonLocation = IntArray(2)
            binding.rvPickForYou.getLocationOnScreen(buttonLocation)

            // Check if scrolling upwards and button is now visible
            if (scrollY < oldScrollY && buttonLocation[1] > 0) {
                Log.d(
                    "Scrolling: Visible",
                    " srollY: $scrollY, oldScrollY: $oldScrollY, buttonLocation: ${buttonLocation[1]}"
                )

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


    private fun setupProductDetailsView(
        item: ProductItemBinding,
        product: RecommendedProductData?
    ) {

        with(item) {
            ratingText.text = product?.averagerating
            ratingBar.rating = product?.averagerating?.toFloatOrNull()
                ?: 0f // Safely convert to float, defaulting to 0
            ratingsCount.text = product?.totalratings
            tvDiscount.text = product?.discountedprice
            tvOrginalPrice.text =
                cutOnText(requireContext().applicationContext, product?.originalprice)
            tvProductName.text = product?.productname

            val htmlContent = createRatingDescription(
                binding.root.context,
                product?.userrating?.review, product?.userrating?.rating
            )

            tvRatingDescription.text = htmlContent
            tvRatingDescription.text = htmlContent
            tvOrginalPrice.text = Html.fromHtml(
                "CA${product?.originalprice}".cutOnText(),
                Html.FROM_HTML_MODE_LEGACY,
                ImageGetter(requireContext()),
                null
            )
        }
    }

    private fun initAdapters() {
        mPickForYouAdapter = PreferenceProductAdapter()
        mYouMightInterestedAdapter = PreferenceProductAdapter()
        mProductAdapter = ProductAdapter()
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        with(binding) {
            rvPickForYou.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = mPickForYouAdapter
            }

            rvYouMightInterested.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = mYouMightInterestedAdapter
            }

            rvFavourite.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = mProductAdapter
            }
        }
    }

    private fun initListeners() {
        binding.inPickForYou.clProduct.setOnClickListener {
//            findNavController().navigate(R.id.action_latestHomeFragment_to_productDetailFragment)
            val action =
                HomeFragmentLatestDirections.actionLatestHomeFragmentToProductDetailFragment()

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
                        setupProductDetailsView(this, bestPick)
                    }
                } ?: run {
                    // Handle the case where bestPick is null, if needed
                    // For example, you can clear the views or set default values
                    clearViews(binding.inPickForYou)
                }

                product?.justForYou?.let { justForYou ->
                    binding.inJustForYou.apply {
                        setupProductDetailsView(this, justForYou)
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

                it?.product?.let { product ->
                    val list = listOf(product)
                    mPickForYouAdapter.submitList(list)
                    mYouMightInterestedAdapter.submitList(list)

                    product?.userrating?.let {

                    }
                }
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

    private fun getRecommendedProducts() {
        viewModel.getRecommendedProducts()
    }

    private fun getPreferenceProductDetail() {
        viewModel.getPreferenceProductDetail()
    }

    override fun getMyViewModel() = viewModel
}
