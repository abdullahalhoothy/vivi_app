package com.app.vivi.features.review

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.data.remote.model.response.review.UserReviewResponse
import com.app.vivi.databinding.FragmentProductFilterListBinding
import com.app.vivi.databinding.FragmentProductReviewBinding
import com.app.vivi.databinding.ProductItemBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.roundToTwoDecimalPlaces
import com.app.vivi.features.filter.adapters.ProductFilterListAdapter
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import com.app.vivi.features.homescreen.home.viewmodels.filter.ProductFilterListViewModel
import com.app.vivi.helper.createRatingDescription
import com.app.vivi.helper.cutOnText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import loadImageWithCache

@AndroidEntryPoint
class ProductReviewFragment : BaseFragmentVB<FragmentProductReviewBinding>(FragmentProductReviewBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val reviewId = arguments?.getInt("reviewId")
        viewModel.sendReviewId(reviewId)

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


    private fun setupProductDetailsView(
        item: FragmentProductReviewBinding,
        product: UserReviewResponse?
    ) {

        with(item.inReviewLayout) {
            item.root.visibility = View.VISIBLE
            tvProductName.text = product?.name
            tvProductDetails.text = product?.description
            tvProductAddress.text = product?.city.plus(", ${product?.country}")

            tvAverageRating.text = product?.averagerating
            /*ratingBar.rating = product?.averagerating?.toFloatOrNull()
                ?: 0f // Safely convert to float, defaulting to 0
            ratingsCount.text = product?.totalratings.plus(" ${getString(R.string.ratings_txt)}")
            val discountedPrice = product?.discountedprice?.toDouble()?.roundToTwoDecimalPlaces()
            tvDiscount.text = "CA$${discountedPrice}"

            inSaveLayout.labelText.text = "${getString(R.string.save_txt)} ${product?.discountpercentage}"*/

            tvPrice.text = "CA$${product?.originalprice}"
//                cutOnText(requireContext().applicationContext, "CA$${product?.originalprice}")

            val htmlContent = createRatingDescription(
                binding.root.context,
                product?.userrating?.description, product?.userrating?.rating
            )
            tvRatingDescription.text = htmlContent
            tvRatingUser.text = product?.userrating?.username
//            tvTime.text = product?.userrating?.

            product?.imageurl?.let { ivProductBackground.loadImageWithCache(it, R.drawable.ic_bg_coffee) }
        }
    }

    private fun initAdapters() {
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        with(binding) {

            val htmlContent = createRatingDescription(
                binding.root.context,
                "Clear deep ruby in color with medium intensity", "3.3"
            )
            inReviewLayout.tvRatingDescription.text = htmlContent
        }
    }

    private fun initListeners() {
        with(binding){
            inAppBar.ivBack.setOnClickListener { findNavController().popBackStack() }

            inReviewLayout.tvComment.setOnClickListener {
                val editText = etComment
                editText.requestFocus()
                // Optionally, show the keyboard when the EditText is focused
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    private fun addObservers() {
        collectWhenStarted {
            viewModel.reviewIdFlow.collectLatest {
                it?.let { reviewId ->
                    getUserReviewsApi(reviewId)                }
            }
        }
        collectWhenStarted {
            viewModel.userReview.collectLatest {
                it?.let { data ->
                    setupProductDetailsView(binding, data)
                }
            }
        }
    }

    private fun getUserReviewsApi(reviewId: Int) {
        viewModel.getUserReviewApi(reviewId)
    }

    override fun getMyViewModel() = viewModel
}
