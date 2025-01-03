package com.app.vivi.domain.repo

import RecommendedProductsResponse
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.request.FilteredProductsRequest
import com.app.vivi.data.remote.model.request.ReviewRequest
import com.app.vivi.data.remote.model.request.ReviewsRequest
import com.app.vivi.data.remote.model.response.FindYourNewFavoriteProductResponse
import com.app.vivi.data.remote.model.response.PreferenceProductResponse
import com.app.vivi.data.remote.model.response.UserReviewsResponse
import com.app.vivi.data.remote.model.response.filter.FilteredProductsResponse
import com.app.vivi.data.remote.model.response.filter.ProductFiltersResponse
import com.app.vivi.data.remote.model.response.review.UserReviewResponse
import com.app.vivi.data.remote.model.response.searchfragment.CoffeeBeanTypesResponse
import com.app.vivi.data.remote.model.response.searchfragment.CountriesResponse
import com.app.vivi.data.remote.model.response.searchfragment.ShopByRegionResponse
import com.app.vivi.data.remote.model.response.searchfragment.ShopByTypeResponse

interface ProductRepo {

    suspend fun getRecommendedProducts(): Resource<RecommendedProductsResponse>

    suspend fun getPreferenceProductDetail(): Resource<PreferenceProductResponse>

    suspend fun getFindYourNewFavoriteProduct(): Resource<FindYourNewFavoriteProductResponse>

    suspend fun getUserReviews(request: ReviewsRequest): Resource<UserReviewsResponse>

    suspend fun getUserReview(request: ReviewRequest): Resource<UserReviewResponse>

    suspend fun getShopByCoffeeTypes(): Resource<ShopByTypeResponse>

    suspend fun getShopByCoffeeBeanTypes(): Resource<CoffeeBeanTypesResponse>

    suspend fun getShopByCountries(): Resource<CountriesResponse>

    suspend fun getShopByRegions(): Resource<ShopByRegionResponse>

    suspend fun getFilteredProductsApi(request: FilteredProductsRequest): Resource<FilteredProductsResponse>

    suspend fun getProductFiltersApi(request: FilteredProductsRequest): Resource<ProductFiltersResponse>

}