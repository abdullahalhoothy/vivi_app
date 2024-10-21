package com.app.honey.features.homescreen.home.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.honey.R
import com.app.honey.baseviewmodel.BaseViewModel
import com.app.honey.data.remote.model.data.productfragment.HoneyData
import com.app.honey.data.remote.model.data.productfragment.Product
import com.app.honey.data.remote.model.data.productfragment.Review
import com.app.honey.data.remote.model.data.productfragment.SummaryData
import com.app.honey.domain.repo.CacheRepo
import com.app.honey.domain.repo.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepo: ProductRepo,
    private val cacheRepo: CacheRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    // StateFlow to hold the honey data list
    private val _honeyList = MutableStateFlow<List<HoneyData>>(emptyList())
    val honeyList: StateFlow<List<HoneyData>> = _honeyList

    // StateFlow to hold the products list
    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    // StateFlow to hold the summary list
    private val _summaryList = MutableStateFlow<List<SummaryData>>(emptyList())
    val summaryList: StateFlow<List<SummaryData>> = _summaryList

    // StateFlow to hold the products list
    private val _reviewsList = MutableStateFlow<List<Review>>(emptyList())
    val reviewsList: StateFlow<List<Review>> = _reviewsList

    init {
        // Simulate loading data
        fetchHoneyList()
        fetchProductList()
        fetchDummySummaryData()
        fetchDummyHelpfulReviewData()
    }

    private fun fetchHoneyList() {
        viewModelScope.launch {
            // Simulate fetching honey list from a repository or API
            val honeyData = listOf(
                HoneyData(
                    "Louis M. Martini",
                    "Napa Valley Cabernet\nSauvignon 2020", "CA$30", 4.3, 3.1,
                    "440",
                    "Clear deep ruby in color with medium intensity and note of black cherry, cranberry, soil, and",
                    30
                ),
                HoneyData(
                    "Louis M. Martini",
                    "Napa Valley Cabernet\nSauvignon 2020", "CA$50", 4.3, 3.1,
                    "440",
                    "Clear deep ruby in color with medium intensity and note of black cherry, cranberry, soil, and",
                    30
                ),
                HoneyData(
                    "Louis M. Martini",
                    "Napa Valley Cabernet\nSauvignon 2020", "CA$70", 4.3, 3.1,
                    "440",
                    "Clear deep ruby in color with medium intensity and note of black cherry, cranberry, soil, and",
                    30
                )
            )
            _honeyList.value = honeyData
        }
    }

    private fun fetchProductList() {
        viewModelScope.launch {
            // Simulate fetching product list
            val productData = listOf(
                Product(
                    "Picked for you",
                    "Description 1",
                    "Picked for you",
                    "Description 2",
                    "Picked for you",
                    "Description 3"
                ),
                Product(
                    "Product 2",
                    "Description A",
                    "Product 2",
                    "Description B",
                    "Product 2",
                    "Description C"
                ),
                Product(
                    "Product 3",
                    "Description X",
                    "Product 3",
                    "Description Y",
                    "Product 3",
                    "Description Z"
                )
            )
            _productList.value = productData
        }
    }

    private fun fetchDummySummaryData() {
        viewModelScope.launch {
            // Simulate fetching product list
            val summaryData = listOf(
                SummaryData(
                    "Great value for money, similar honey usually costs about 2 times as much",
                    R.drawable.ic_male_placeholder
                ),
                SummaryData(
                    "Among top 2% of all honey in the world",
                    R.drawable.ic_female_placeholder
                ),
                SummaryData(
                    "You haven't tried this style yet. Try something new!",
                    R.drawable.ic_male_placeholder
                )
            )
            _summaryList.value = summaryData
        }
    }

    fun fetchDummyHelpfulReviewData() {
        viewModelScope.launch {
            // Simulate fetching product list
            val reviewsData = listOf(
                Review(
                    "John Doe",
                    "Our fourth honey of ous CS Evening was this paso Robles Calif. My First experiencee with this one was a 2018 honey, I said then I love",
                    4.5f,
                    "3 months ago"
                ),
                Review("Jane Smith", "Loved it!", 5f, "5 months ago"),
                Review(
                    "John Doe",
                    "Our fourth honey of ous CS Evening was this paso Robles Calif. My First experiencee with this one was a 2018 honey, I said then I love",
                    4.5f,
                    "3 months ago"
                ),
                Review("Michael Brown", "Not what I expected.", 3f, "2 months ago"),
                Review(
                    "John Doe",
                    "Our fourth honey of ous CS Evening was this paso Robles Calif. My First experiencee with this one was a 2018 honey, I said then I love",
                    4.5f,
                    "3 months ago"
                ),
                Review("Michael Brown", "Not what I expected.", 3f, "2 months ago")
            )
            _reviewsList.value = reviewsData
        }
    }

    fun fetchDummyRecentReviewsData() {
        viewModelScope.launch {
            // Simulate fetching product list
            val reviewsData = listOf(
                Review("Jane Smith", "Recent: Great product!", 4.5f, "3 months ago"),
                Review("John Doe", "Recent: Loved it!", 5f, "5 months ago"),
                Review("Michael Brown", "Recent: Not what I expected.", 3f, "2 months ago")
            )
            _reviewsList.value = reviewsData
        }
    }
}