package com.app.vivi.features.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.databinding.FragmentReviewsBinding
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.navigateWithSingleTop
import com.app.vivi.features.homescreen.home.adapters.ReviewsAdapter
import com.app.vivi.features.homescreen.home.fragments.ProductDetailFragmentDirections
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ReviewsFragment : BaseFragmentVB<FragmentReviewsBinding>(FragmentReviewsBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()

    private val reviewsAdapter by lazy {
        ReviewsAdapter(onCommentClick = {
            navigateToReviewCommentFragment()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapters()
        initListeners()
        addObservers()
        getUserReviewsApi("All")
    }


    private fun initViews() {
        binding.apply {
            inAppBar.textView.text = getString(R.string.all_reviews_txt)
        }

    }

    private fun initAdapters() {
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        with(binding) {

            rvReviews.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = reviewsAdapter
            }
        }
    }

    private fun initListeners() {

    }

    private fun navigateToReviewCommentFragment(){
        val action = ProductDetailFragmentDirections.actionProductDetailFragmentToProductReviewFragmentt()
        findNavController().navigateWithSingleTop(action)
    }

    private fun addObservers() {
        collectWhenStarted {
            viewModel.userReviews.collectLatest {
                it?.reviews.let { reviewList ->
                    reviewsAdapter.submitList(reviewList)
                }
            }
        }
    }

    private fun getUserReviewsApi(type: String) {
        viewModel.getUserReviewsApi(type)
    }

    override fun getMyViewModel() = viewModel
}
