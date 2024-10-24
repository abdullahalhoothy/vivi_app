package com.app.vivi.domain.repo

import RecommendedProductsResponse
import com.app.vivi.data.remote.Resource

interface ProductRepo {


//    suspend fun getSig(request: GetSigRequest): Resource<GetSigResponse>

    suspend fun getRecommendedProducts(): Resource<RecommendedProductsResponse>
}