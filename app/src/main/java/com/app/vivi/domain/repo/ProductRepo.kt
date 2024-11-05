package com.app.vivi.domain.repo

import RecommendedProductsResponse
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.response.FindYourNewFavoriteProductResponse
import com.app.vivi.data.remote.model.response.PreferenceProductResponse
import com.app.vivi.data.remote.model.response.UserReviewsResponse

interface ProductRepo {

    suspend fun getRecommendedProducts(): Resource<RecommendedProductsResponse>

    suspend fun getPreferenceProductDetail(): Resource<PreferenceProductResponse>

    suspend fun getFindYourNewFavoriteProduct(): Resource<FindYourNewFavoriteProductResponse>

    suspend fun getUserReviews(): Resource<UserReviewsResponse>

}