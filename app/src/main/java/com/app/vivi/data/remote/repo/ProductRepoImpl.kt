package com.app.vivi.data.remote.repo

import RecommendedProductsResponse
import com.app.vivi.data.remote.ApiService
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.response.FindYourNewFavoriteProductResponse
import com.app.vivi.data.remote.model.response.PreferenceProductResponse
import com.app.vivi.data.remote.model.response.UserReviewsResponse
import com.app.vivi.data.remote.model.response.searchfragment.CoffeeBeanTypesResponse
import com.app.vivi.data.remote.model.response.searchfragment.CountriesResponse
import com.app.vivi.data.remote.model.response.searchfragment.ShopByTypeResponse
import com.app.vivi.domain.repo.CacheRepo
import com.app.vivi.domain.repo.ProductRepo
import javax.inject.Inject

class ProductRepoImpl @Inject constructor(apiService: ApiService, val cacheRepo: CacheRepo) :
    ProductRepo,
    BaseRepo(apiService) {

    override suspend fun getRecommendedProducts(): Resource<RecommendedProductsResponse> {
        return safeApiCall {
            apiService.getRecommendedProducts()
        }
    }

    override suspend fun getPreferenceProductDetail(): Resource<PreferenceProductResponse> {
        return safeApiCall { apiService.getPreferenceProductDetail() }
    }

    override suspend fun getFindYourNewFavoriteProduct(): Resource<FindYourNewFavoriteProductResponse> {
        return safeApiCall { apiService.getFindYourNewFavoriteProduct() }
    }

    override suspend fun getUserReviews(): Resource<UserReviewsResponse> {
        return safeApiCall { apiService.getUserReviews() }
    }

    override suspend fun getShopByCoffeeTypes(): Resource<ShopByTypeResponse> {
        return safeApiCall { apiService.getShopByCoffeeTypes() }
    }

    override suspend fun getShopByCoffeeBeanTypes(): Resource<CoffeeBeanTypesResponse> {
        return safeApiCall { apiService.getShopByCoffeeBeanTypes() }
    }

    override suspend fun getShopByCountries(): Resource<CountriesResponse> {
        return safeApiCall { apiService.getShopByCountries() }
    }


}