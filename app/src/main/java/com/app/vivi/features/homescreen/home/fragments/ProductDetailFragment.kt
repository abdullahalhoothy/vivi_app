package com.app.vivi.features.homescreen.home.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vivi.R
import com.app.vivi.basefragment.BaseFragmentVB
import com.app.vivi.customviews.CircleDrawable
import com.app.vivi.customviews.CustomTextView
import com.app.vivi.data.remote.model.data.productfragment.SummaryData
import com.app.vivi.databinding.FragmentProductDetailBinding
import com.app.vivi.dialog.rating.RatingDialogHelper
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.navigateWithSingleTop
import com.app.vivi.features.homescreen.home.adapters.ReviewsAdapter
import com.app.vivi.features.homescreen.home.adapters.SummaryAdapter
import com.app.vivi.features.homescreen.home.adapters.productdetail.CharacteristicsAdapter
import com.app.vivi.features.homescreen.home.adapters.productdetail.ExpandableAdapter
import com.app.vivi.features.homescreen.home.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailFragment :
    BaseFragmentVB<FragmentProductDetailBinding>(FragmentProductDetailBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel>()

    private val summaryAdapter by lazy {
        SummaryAdapter {
            handleItemClick(it)
        }
    }

    private val reviewsAdapter by lazy {
        ReviewsAdapter()
    }

    private val characteristicsAdapter by lazy {
        CharacteristicsAdapter()
    }

    private val thoughtsAdapter by lazy {
        ExpandableAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupAdapters()
        initListeners()
        addObservers()
        handleBackPress()
        handleAppBar()
        handleOnScrollChangeListener()
        animateImage()
        getUserReviewsApi()
    }


    private fun setupUI() {
        binding.ivPreference.background = CircleDrawable(
            ContextCompat.getColor(requireContext(), R.color.red)
        )
    }

    private fun setupAdapters() {
        with(binding){
            inSummaryLayout.rvSummary.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = summaryAdapter
            }
            inReviewsLayout.rvReviews.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = reviewsAdapter
            }

            inTasteCharacteristicsLayout.rvCharacteristics.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = characteristicsAdapter
            }

            inTasteCharacteristicsLayout.rvThoughts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = thoughtsAdapter
            }
        }

    }

    private fun initListeners() {
        binding.apply {
            inAppBar.centerImageView.setOnClickListener {
                val action = ProductDetailFragmentDirections.actionLatestHomeFragmentToNotificationsFragment()
                findNavController().navigateWithSingleTop(action)
            }

            tvRate.setOnClickListener {
                showRatingDialog()
            }

            inSummaryLayout.apply {
                tvHighllights.setOnClickListener { toggleSummaryView(true) }
                tvFacts.setOnClickListener { toggleSummaryView(false) }
            }

            inReviewsLayout.apply {
                tvHelpful.setOnClickListener { toggleReviewsView(true) }
                tvRecent.setOnClickListener { toggleReviewsView(false) }
            }
        }


    }

    private fun toggleSummaryView(showSummary: Boolean) {
        binding.inSummaryLayout.apply {
            rvSummary.visibility = if (showSummary) View.VISIBLE else View.INVISIBLE
            clFacts.visibility = if (showSummary) View.INVISIBLE else View.VISIBLE
            updateSelection(if (showSummary) tvHighllights else tvFacts)
        }
    }

    private fun toggleReviewsView(showReviews: Boolean) {
        binding.inReviewsLayout.apply {
            if (showReviews) getUserReviewsApi()
            if (!showReviews) getUserReviewsApi()
            updateReviewsSelection(if (showReviews) tvHelpful else tvRecent)
        }
    }

    private fun updateSelection(selectedTextView: CustomTextView) {
        binding.inSummaryLayout.apply {
            listOf(tvHighllights, tvFacts).forEach {
                it.updateBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.greyLight
                    )
                )
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            }
            selectedTextView.apply {
                updateBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            }
        }
    }

    private fun updateReviewsSelection(selectedTextView: CustomTextView) {
        binding.inReviewsLayout.apply {
            listOf(tvHelpful, tvRecent).forEach {
                it.updateBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.greyLight
                    )
                )
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            }
            selectedTextView.apply {
                updateBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            }
        }
    }

    private fun handleBackPress() {
        binding.inAppBar.ivBack.setOnClickListener { findNavController().popBackStack() }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    private fun startAnimation() {
        binding?.ivProductBackground?.let { view ->
            if (view.isAttachedToWindow) {
                view.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // Remove the listener to avoid multiple calls
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        view.translationX = -view.width.toFloat()
                        view.scaleX = 0.5f
                        view.scaleY = 0.5f
                        view.animate().translationX(0f).scaleX(1f).scaleY(1f).setDuration(800)
                            .start()

                    }
                })
            }
        }
    }


    private fun animateImage() {
        // Create a DefaultLifecycleObserver to handle lifecycle events
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                startAnimation()
            }
        }

        // Add the observer to the lifecycle of the viewLifecycleOwner
        viewLifecycleOwner.lifecycle.addObserver(observer)
    }

    private fun handleAppBar() {
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset) / scrollRange.toFloat()

            when (percentage) {
                1f -> updateAppBar("Black Coffee", R.color.colorPrimary)
                0f -> updateAppBar("", R.color.colorTransparent)
            }
        }
    }

    private fun handleOnScrollChangeListener() {
        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            // Get the location of the scrollable Add to Cart button
            val buttonLocation = IntArray(2)
//            addToCartButtonScroll.getLocationOnScreen(buttonLocation)
            binding.tvAddToCart.getLocationOnScreen(buttonLocation)

            // Check if the button has moved out of view
            if (scrollY > oldScrollY && buttonLocation[1] < 0) {
                // Button is scrolled out of view, show the fixed button
                binding.clFixeAddToCart.visibility = View.VISIBLE
            } else if (scrollY < oldScrollY && buttonLocation[1] > 0) {
                // Button is still in view, hide the fixed button
                binding.clFixeAddToCart.visibility = View.GONE
            }
        }
    }

    private fun updateAppBar(title: String, @ColorRes backgroundColor: Int) {
        binding.inAppBar.textView.text = title
        binding.toolbar.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                backgroundColor
            )
        )
    }

    private fun handleItemClick(item: SummaryData) {
        // Navigate or handle item click
    }


    // Inside an Activity or Fragment
    private val ratingDialogHelper by lazy {
        RatingDialogHelper(requireContext()) { rating, review ->
            // Handle review submission logic here
        }
    }

    private fun showRatingDialog() {
        ratingDialogHelper.showRatingDialog()
    }


    private fun addObservers() {
        collectWhenStarted {
            viewModel.summaryList.collectLatest { summaryList ->
                summaryAdapter.submitList(summaryList)
            }
        }

        collectWhenStarted {
            viewModel.CharacteristicsDataList.collectLatest { list ->
                characteristicsAdapter.submitList(list)
            }
        }

        collectWhenStarted {
            viewModel.ThoughtsDataList.collectLatest { list ->
                thoughtsAdapter.submitList(list)
            }
        }

        collectWhenStarted {
            viewModel.userReviews.collectLatest {
                it?.reviews.let {reviewList ->
                    reviewsAdapter.submitList(reviewList)
                }
            }
        }
    }

    private fun getUserReviewsApi(){
        viewModel.getUserReviewsApi()
    }

    override fun getMyViewModel() = viewModel
}
