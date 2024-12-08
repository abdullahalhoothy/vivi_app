package com.app.vivi.features.homescreen.home.viewmodels

import RecommendedProductsResponse
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.vivi.R
import com.app.vivi.baseviewmodel.BaseViewModel
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.data.productdetailfragment.ProductMakingCountries
import com.app.vivi.data.remote.model.data.productdetailfragment.ThoughtsData
import com.app.vivi.data.remote.model.data.productdetailfragment.Vintage
import com.app.vivi.data.remote.model.data.productfragment.CharacteristicsData
import com.app.vivi.data.remote.model.data.productfragment.SummaryData
import com.app.vivi.data.remote.model.request.ReviewRequest
import com.app.vivi.data.remote.model.request.ReviewsRequest
import com.app.vivi.data.remote.model.response.NewFavoriteProduct
import com.app.vivi.data.remote.model.response.PreferenceProductResponse
import com.app.vivi.data.remote.model.response.products
import com.app.vivi.data.remote.model.response.UserRating
import com.app.vivi.data.remote.model.response.UserReviewsResponse
import com.app.vivi.data.remote.model.response.review.UserReviewResponse
import com.app.vivi.data.remote.model.response.searchfragment.CoffeeBeanType
import com.app.vivi.data.remote.model.response.searchfragment.CoffeeBeanTypesResponse
import com.app.vivi.data.remote.model.response.searchfragment.CountriesResponse
import com.app.vivi.data.remote.model.response.searchfragment.ProductType
import com.app.vivi.data.remote.model.response.searchfragment.ShopByRegionResponse
import com.app.vivi.data.remote.model.response.searchfragment.ShopByTypeResponse
import com.app.vivi.domain.model.ErrorModel
import com.app.vivi.domain.repo.CacheRepo
import com.app.vivi.domain.repo.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepo: ProductRepo,
    private val cacheRepo: CacheRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    private val _reviewIdFlow = MutableStateFlow<Int?>(null)
    val reviewIdFlow = _reviewIdFlow.asStateFlow()

    // StateFlow to hold the products list
    private val _characteristicsDataList = MutableStateFlow<List<CharacteristicsData>>(emptyList())
    val CharacteristicsDataList: StateFlow<List<CharacteristicsData>> = _characteristicsDataList

    // StateFlow to hold the products list
    private val _productMakingCountriesListList = MutableStateFlow<List<ProductMakingCountries>>(emptyList())
    val productMakingCountriesList: StateFlow<List<ProductMakingCountries>> = _productMakingCountriesListList
    // StateFlow to hold the products list
    private val _thoughtsDataList = MutableStateFlow<List<ThoughtsData>>(emptyList())
    val ThoughtsDataList: StateFlow<List<ThoughtsData>> = _thoughtsDataList
    // StateFlow to hold the products list
    private val _vintageDataList = MutableStateFlow<List<Vintage>>(emptyList())
    val vintageDataList: StateFlow<List<Vintage>> = _vintageDataList

    // StateFlow to hold the summary list
    private val _summaryList = MutableStateFlow<List<SummaryData>>(emptyList())
    val summaryList: StateFlow<List<SummaryData>> = _summaryList

    // StateFlow to hold the products list
    private val _recommendedProducts = MutableStateFlow<RecommendedProductsResponse?>(null)
    val recommendedProducts: StateFlow<RecommendedProductsResponse?> = _recommendedProducts

    // StateFlow to hold the products list
    private val _preferenceProductDetail = MutableStateFlow<PreferenceProductResponse?>(null)
    val preferenceProductDetail: StateFlow<PreferenceProductResponse?> = _preferenceProductDetail

    // StateFlow to hold the products list
    private val _bestOfProductsDetail = MutableStateFlow<List<products>?>(null)
    val bestOfProductsDetail: StateFlow<List<products>?> = _bestOfProductsDetail

    // StateFlow to hold the products list
    private val _findYourNewFavoriteProduct = MutableStateFlow<List<List<NewFavoriteProduct>>?>(null)
    val findYourNewFavoriteProduct: StateFlow<List<List<NewFavoriteProduct>>?> = _findYourNewFavoriteProduct

    // StateFlow to hold the products list
    private val _userReviews = MutableStateFlow<UserReviewsResponse?>(null)
    val userReviews: StateFlow<UserReviewsResponse?> = _userReviews

    // StateFlow to hold the products list
    private val _userReview = MutableStateFlow<UserReviewResponse?>(null)
    val userReview: StateFlow<UserReviewResponse?> = _userReview
    // StateFlow to hold the products list
    private val _shopByType = MutableStateFlow<List<List<ProductType>>?>(null)
    val shopByType: StateFlow<List<List<ProductType>>?> = _shopByType
    // StateFlow to hold the products list
    private val _shopByCoffeeBeanTypes = MutableStateFlow<List<List<CoffeeBeanType>>?>(null)
    val shopByCoffeeBeanTypes: StateFlow<List<List<CoffeeBeanType>>?> = _shopByCoffeeBeanTypes
    // StateFlow to hold the products list
    private val _shopByCountries = MutableStateFlow<CountriesResponse?>(null)
    val shopByCountries: StateFlow<CountriesResponse?> = _shopByCountries
    // StateFlow to hold the products list
    private val _shopByRegion = MutableStateFlow<ShopByRegionResponse?>(null)
    val shopByRegion: StateFlow<ShopByRegionResponse?> = _shopByRegion

    init {
        // Simulate loading data
        fetchProductList()
        fetchThoughtsList()
        fetchDummySummaryData()
        generateSampleProducts()
        fetchRecentList()
    }


    fun sendReviewId(reviewId: Int?) {
        viewModelScope.launch {
            _reviewIdFlow.value = reviewId
        }
    }

    private fun fetchProductList() {
        viewModelScope.launch {
            // Simulate fetching product list
            val productData = listOf(
                CharacteristicsData(id = 1, labelLight = "Light", labelBold = "Bold", sliderValue = 50f),
                CharacteristicsData(id = 2, labelLight = "Small", labelBold = "Large", sliderValue = 30f),
                CharacteristicsData(id = 3, labelLight = "Dim", labelBold = "Bright", sliderValue = 75f),
                CharacteristicsData(id = 2, labelLight = "Small", labelBold = "Large", sliderValue = 30f),
            )
            _characteristicsDataList.value = productData
        }
    }


    private fun fetchThoughtsList() {
        viewModelScope.launch {
            // Simulate fetching product list
            val productData = listOf(
                ThoughtsData("Citrus, lemon, grapefruit", "57 mentions of citrus fruit notes"),
                ThoughtsData("Peach, apricot, pear", "37 mentions of tree fruit notes"),
                ThoughtsData("Stone, honey, minerals", "25 mentions of earthy notes"),
                ThoughtsData("Additional Item 2", "Description for additional item 2"),
                ThoughtsData("Additional Item 1", "Description for additional item 1")
            )
            _thoughtsDataList.value = productData
        }
    }


    fun fetchRecentList() {
        viewModelScope.launch {
            // Simulate fetching product list
             val recentData = listOf(
                Vintage("2020", "4.0", "344 ratings"),
                Vintage("2019", "4.1", "886 ratings"),
                Vintage("2018", "4.1", "1769 ratings"),
                Vintage("2017", "4.0", "514 ratings"),
                Vintage("2016", "4.1", "1218 ratings")
            )

            _vintageDataList.value = recentData
        }
    }
    fun fetchBestPriceList() {
        viewModelScope.launch {
            // Simulate fetching product list

            val bestPriceData = listOf(
                Vintage("2019", "4.3", "786 ratings"),
                Vintage("2018", "4.2", "1569 ratings")
            )

            _vintageDataList.value = bestPriceData
        }
    }
    fun fetchTopRatingList() {
        viewModelScope.launch {
            // Simulate fetching product list

            val topRatingData = listOf(
                Vintage("2020", "4.5", "124 ratings"),
                Vintage("2018", "4.6", "1299 ratings")
            )

            _vintageDataList.value = topRatingData
        }
    }

    private fun fetchDummySummaryData() {
        viewModelScope.launch {
            // Simulate fetching product list
            val summaryData = listOf(
                SummaryData(
                    "Great value for money, similar vivi usually costs about 2 times as much",
                    R.drawable.ic_male_placeholder
                ),
                SummaryData(
                    "Among top 2% of all vivi in the world",
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


    fun generateSampleProducts() {
        viewModelScope.launch {
            val list = listOf(
                products(
                    id = "1",
                    name = "Product A",
                    description = "Description of Product A",
                    tagline = "Best product A",
                    producturl = "http://example.com/productA",
                    imageurl = "http://example.com/imageA.jpg",
                    ratingtext = "Excellent",
                    ratingvalue = "4.5",
                    averagerating = "4.5",
                    totalratings = "100",
                    discountedprice = "10.99",
                    discountpercentage = "20",
                    originalprice = "13.99",
                    city = "City A",
                    country = "Country A",
                    countryflagurl = "http://example.com/flagA.png",
                    userrating = UserRating(
                        rating = "4.5",
                        review = "Great product!",
                        username = "User1",
                        description = "User1 review description",
                        userimageurl = "http://example.com/user1.png"
                    )
                ),
                products(
                    id = "2",
                    name = "Product B",
                    description = "Description of Product B",
                    tagline = "Best product B",
                    producturl = "http://example.com/productB",
                    imageurl = "http://example.com/imageB.jpg",
                    ratingtext = "Very Good",
                    ratingvalue = "4.2",
                    averagerating = "4.2",
                    totalratings = "150",
                    discountedprice = "15.99",
                    discountpercentage = "10",
                    originalprice = "17.99",
                    city = "City B",
                    country = "Country B",
                    countryflagurl = "http://example.com/flagB.png",
                    userrating = UserRating(
                        rating = "4.2",
                        review = "Very good product!",
                        username = "User2",
                        description = "User2 review description",
                        userimageurl = "http://example.com/user2.png"
                    )
                ),
                products(
                    id = "1",
                    name = "Product A",
                    description = "Description of Product A",
                    tagline = "Best product A",
                    producturl = "http://example.com/productA",
                    imageurl = "http://example.com/imageA.jpg",
                    ratingtext = "Excellent",
                    ratingvalue = "4.5",
                    averagerating = "4.5",
                    totalratings = "100",
                    discountedprice = "10.99",
                    discountpercentage = "20",
                    originalprice = "13.99",
                    city = "City A",
                    country = "Country A",
                    countryflagurl = "http://example.com/flagA.png",
                    userrating = UserRating(
                        rating = "4.5",
                        review = "Great product!",
                        username = "User1",
                        description = "User1 review description",
                        userimageurl = "http://example.com/user1.png"
                    )
                ),
                products(
                    id = "2",
                    name = "Product B",
                    description = "Description of Product B",
                    tagline = "Best product B",
                    producturl = "http://example.com/productB",
                    imageurl = "http://example.com/imageB.jpg",
                    ratingtext = "Very Good",
                    ratingvalue = "4.2",
                    averagerating = "4.2",
                    totalratings = "150",
                    discountedprice = "15.99",
                    discountpercentage = "10",
                    originalprice = "17.99",
                    city = "City B",
                    country = "Country B",
                    countryflagurl = "http://example.com/flagB.png",
                    userrating = UserRating(
                        rating = "4.2",
                        review = "Very good product!",
                        username = "User2",
                        description = "User2 review description",
                        userimageurl = "http://example.com/user2.png"
                    )
                )
            )

            _bestOfProductsDetail.value = list
        }
    }


    fun getRecommendedProducts() {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getRecommendedProducts()) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    _recommendedProducts.value = call.data
                    /*if (call.data.responseCode == 200) {
                        _recommendedProducts.value = call.data.data
                    } else {
                        showError(ErrorModel("Error", call.data.message.toString()))
                    }*/
                }
            }
        }
    }

    fun getPreferenceProductDetail() {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getPreferenceProductDetail()) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    _preferenceProductDetail.value = call.data
                }
            }
        }
    }

    fun getFindYourNewFavoriteProduct() {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getFindYourNewFavoriteProduct()) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    val newItems: List<List<NewFavoriteProduct>> = call.data.products?.chunked(3) ?: emptyList()

                    _findYourNewFavoriteProduct.value = newItems
                }
            }
        }
    }

    fun getUserReviewsApi(type: String) {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getUserReviews(ReviewsRequest(type))) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    _userReviews.value = call.data
                }
            }
        }
    }

    fun getUserReviewApi(reviewId: Int) {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getUserReview(ReviewRequest(reviewId))) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    _userReview.value = call.data
                }
            }
        }
    }

    fun getShopByCoffeeTypes() {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getShopByCoffeeTypes()) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    val newItems: List<List<ProductType>> = call.data.products?.chunked(3) ?: emptyList()
                    _shopByType.value = newItems
                }
            }
        }
    }

    fun getShopByCoffeeBeanTypes() {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getShopByCoffeeBeanTypes()) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    val newItems: List<List<CoffeeBeanType>> = call.data.coffeeBeanTypes?.chunked(3) ?: emptyList()
                    _shopByCoffeeBeanTypes.value = newItems
                }
            }
        }
    }

    fun getShopByCountries() {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getShopByCountries()) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    _shopByCountries.value = call.data
                }
            }
        }
    }

    fun getShopByRegions() {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getShopByRegions()) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    _shopByRegion.value = call.data
                }
            }
        }
    }
}