package com.app.vivi.features.homescreen.home.viewmodels.filter

import RecommendedProductsResponse
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.vivi.R
import com.app.vivi.baseviewmodel.BaseViewModel
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.data.filter.ProductFilterData
import com.app.vivi.data.remote.model.data.productdetailfragment.ProductMakingCountries
import com.app.vivi.data.remote.model.data.productdetailfragment.ThoughtsData
import com.app.vivi.data.remote.model.data.productdetailfragment.Vintage
import com.app.vivi.data.remote.model.data.productfragment.CharacteristicsData
import com.app.vivi.data.remote.model.data.productfragment.SummaryData
import com.app.vivi.data.remote.model.request.FilteredProductsRequest
import com.app.vivi.data.remote.model.response.NewFavoriteProduct
import com.app.vivi.data.remote.model.response.PreferenceProductResponse
import com.app.vivi.data.remote.model.response.products
import com.app.vivi.data.remote.model.response.UserRating
import com.app.vivi.data.remote.model.response.UserReviewsResponse
import com.app.vivi.data.remote.model.response.filter.FilteredProductsResponse
import com.app.vivi.data.remote.model.response.filter.FilteredProductsResponse.FilteredProduct
import com.app.vivi.data.remote.model.response.searchfragment.CoffeeBeanType
import com.app.vivi.data.remote.model.response.searchfragment.CountriesResponse
import com.app.vivi.data.remote.model.response.searchfragment.ProductType
import com.app.vivi.data.remote.model.response.searchfragment.ShopByRegionResponse
import com.app.vivi.domain.model.ErrorModel
import com.app.vivi.domain.repo.CacheRepo
import com.app.vivi.domain.repo.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductFilterListViewModel @Inject constructor(
    private val productRepo: ProductRepo,
    private val cacheRepo: CacheRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    // StateFlow to hold the products list
    private val _productFilterList = MutableStateFlow<List<ProductFilterData>>(emptyList())
    val productFilterList: StateFlow<List<ProductFilterData>> = _productFilterList

    // StateFlow to hold the products list
    private val _filteredProductList = MutableStateFlow<List<FilteredProduct>?>(emptyList())
    val filteredProductList: StateFlow<List<FilteredProduct>?> = _filteredProductList

    private val _findYourNewFavoriteProduct = MutableStateFlow<List<List<NewFavoriteProduct>>?>(null)
    val findYourNewFavoriteProduct: StateFlow<List<List<NewFavoriteProduct>>?> = _findYourNewFavoriteProduct

    init {
        // Simulate loading data
        fetchProductList()
    }


    private fun fetchProductList() {
        viewModelScope.launch {
            // Simulate fetching product list
            // Submit a list of products
            val products = listOf(
                ProductFilterData(
                    id = 1,
                    name = "Beaulieu Vineyard (BV)",
                    description = "Napa Valley Cabernet Sauvignon 2020",
                    address = "Napa Valley, United States",
                    discountPrice = "CA$44.99",
                    originalPrice = "59.99",
                    rating = 4.1f,
                    ratingsCount = "356 ratings",
                    backgroundImage = R.drawable.ic_bg_coffee,
                    bottleImage = R.drawable.ic_bottle,
                    userImage = R.drawable.ic_male_placeholder,
                    recommendation = "Based on your taste and the magic of deep knowledge",
                    ratingUser = "Michael Miller (328 ratings)"
                ),
            ProductFilterData(
                id = 2,
                name = "Château Margaux",
                description = "Bordeaux Red Wine 2015",
                address = "Bordeaux, France",
                discountPrice = "CA$99.99",
                originalPrice = "129.99",
                rating = 4.5f,
                ratingsCount = "450 ratings",
                backgroundImage = R.drawable.ic_bg_coffee,
                bottleImage = R.drawable.ic_bottle,
                userImage = R.drawable.ic_female_placeholder,
                recommendation = "A classic choice for a wine connoisseur.",
                ratingUser = "Sophia Johnson (512 ratings)"
            ),
            ProductFilterData(
                id = 3,
                name = "Yellow Tail Shiraz",
                description = "Rich Australian Shiraz 2019",
                address = "Hunter Valley, Australia",
                discountPrice = "CA$14.99",
                originalPrice = "19.99",
                rating = 3.8f,
                ratingsCount = "220 ratings",
                backgroundImage = R.drawable.ic_bg_coffee,
                bottleImage = R.drawable.ic_bottle,
                userImage = R.drawable.ic_male_placeholder,
                recommendation = "Perfect for casual gatherings.",
                ratingUser = "James Smith (150 ratings)"
            )
            )
            _productFilterList.value = products
        }
    }


    fun getFilteredProductsApi(request: FilteredProductsRequest) {
        viewModelScope.launch {
            showLoader()
            when (val call = productRepo.getFilteredProductsApi(request)) {
                is Resource.Error -> {
                    if (call.code == 401) {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    } else {
                        showError(ErrorModel(title = call.title, message = call.message, call.code))
                    }
                }

                is Resource.Success -> {
                    hideLoader()
                    _filteredProductList.value = call.data.products
                }
            }
        }
    }
}