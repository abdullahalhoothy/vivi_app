package com.app.vivi.domain.repo

import RecommendedProductsResponse
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.response.PreferenceProductResponse

interface ProductRepo {

    suspend fun getRecommendedProducts(): Resource<RecommendedProductsResponse>

    suspend fun getPreferenceProductDetail(): Resource<PreferenceProductResponse>

}