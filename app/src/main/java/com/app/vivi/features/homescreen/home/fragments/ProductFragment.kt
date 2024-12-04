package com.app.vivi.features.homescreen.home.fragments

import RecommendedProductData
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentProductBinding
import com.app.vivi.databinding.ProductItemBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.navigateWithSingleTop
import com.app.vivi.extension.roundToTwoDecimalPlaces
import com.app.vivi.extension.showShortToast
import com.app.vivi.features.homescreen.home.adapters.PreferenceProductAdapter
import com.app.vivi.features.homescreen.home.adapters.ProductOuterFavoriteAdapter
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import com.app.vivi.helper.createRatingDescription
import com.app.vivi.helper.cutOnText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import loadImageWithCache

@AndroidEntryPoint
class ProductFragment : BaseFragmentVB<FragmentProductBinding>(FragmentProductBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()
//    private lateinit var mProductAdapter: ProductAdapter

    private val mPickForYouAdapter by lazy {
        PreferenceProductAdapter(onItemClick = {
            navigateToProductDetailFragment()
        }, onDiscountButtonClick = {
            "Discount Button Clicked".showShortToast(requireContext())
        })
    }

    private val mYouMightInterestedAdapter by lazy {
        PreferenceProductAdapter(onItemClick = {
            navigateToProductDetailFragment()
        }, onDiscountButtonClick = {
            "Discount Button Clicked".showShortToast(requireContext())
        })
    }

    private val mProductOuterFavoriteAdapter by lazy {
        ProductOuterFavoriteAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapters()
        handleOnScrollChangeListener()
        initListeners()
        addObservers()
        getRecommendedProducts()
        getPreferenceProductDetail()
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
//                    getPreferenceProductDetail()
                    getFindYourNewFavoriteProduct()
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
            item.root.visibility = View.VISIBLE
            tvProductName.text = product?.name
            tvProductDetails.text = product?.description
            tvProductAddress.text = product?.city.plus(", ${product?.country}")
            ratingText.text = product?.averagerating
            ratingBar.rating = product?.averagerating?.toFloatOrNull()
                ?: 0f // Safely convert to float, defaulting to 0
            ratingsCount.text = product?.totalratings.plus(" ${getString(R.string.ratings_txt)}")
            val discountedPrice = product?.discountedprice?.toDouble()?.roundToTwoDecimalPlaces()
            tvDiscount.text = "CA$${discountedPrice}"

            inSaveLayout.labelText.text = "${getString(R.string.save_txt)} ${product?.discountpercentage}"

            tvOrginalPrice.text =
                cutOnText(requireContext().applicationContext, "CA$${product?.originalprice}")

            val htmlContent = createRatingDescription(
                binding.root.context,
                product?.userrating?.description, product?.userrating?.rating
            )
            tvRatingDescription.text = htmlContent
            tvRatingUser.text = product?.userrating?.userName

//            "https://drive.google.com/uc?export=view&id=11oOuA4j9MlB1XGLN2uKIuZCeklrFzqZO".let { ivBottle.loadImageWithCache(it) }
//            "https://drive.google.com/uc?id=11oOuA4j9MlB1XGLN2uKIuZCeklrFzqZO".let { ivBottle.loadImageWithCache(it, R.drawable.ic_bottle) }
            product?.producturl?.let { ivBottle.loadImageWithCache(it, R.drawable.ic_bottle) }
            product?.imageurl?.let { ivProductBackground.loadImageWithCache(it, R.drawable.ic_bg_coffee) }
        }
    }

    private fun initAdapters() {
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        with(binding) {
            rvPickForYou.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
                adapter = mProductOuterFavoriteAdapter
            }
        }
    }

    private fun initListeners() {
        binding.inPickForYou.clProduct.setOnClickListener {
            navigateToProductDetailFragment()
        }
        binding.inJustForYou.clProduct.setOnClickListener {
            navigateToProductDetailFragment()
        }

        binding.inJustForYou.tvDiscount.setOnClickListener {
            "Add To Cart Dialog".showShortToast(requireContext())
        }

        binding.inPickForYou.tvDiscount.setOnClickListener {
            "Add To Cart Dialog".showShortToast(requireContext())
        }
    }

    private fun navigateToProductDetailFragment(){
        val action =
            HomeFragmentLatestDirections.actionLatestHomeFragmentToProductDetailFragment()
        findNavController().navigateWithSingleTop(action)
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

                //Loading Banner image
                product?.bannerImageUrl?.let {
                    binding.ivBanner.visibility = View.VISIBLE
                    binding.ivBanner.loadImageWithCache(it, R.drawable.ic_banner) }
            }
        }

        collectWhenStarted {
            viewModel.preferenceProductDetail.collectLatest {

                it?.products?.let { list ->
//                    val list = listOf(product)
                    mPickForYouAdapter.submitList(list)
                    mYouMightInterestedAdapter.submitList(list)

                }
            }
        }

        collectWhenStarted {
            viewModel.findYourNewFavoriteProduct.collectLatest {

                it?.let { productList ->
                    mProductOuterFavoriteAdapter.submitList(productList)

                }
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

    private fun getFindYourNewFavoriteProduct() {
        viewModel.getFindYourNewFavoriteProduct()
    }

    override fun getMyViewModel() = viewModel
}
