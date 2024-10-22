package com.app.honey.domain.repo

import RecommendedProductsResponse
import com.app.honey.data.remote.Resource
import com.app.honey.data.remote.model.request.*
import com.app.honey.data.remote.model.response.*
import com.app.honey.data.remote.model.response.common.CommonResponse

interface ProductRepo {


//    suspend fun getSig(request: GetSigRequest): Resource<GetSigResponse>

    suspend fun getRecommendedProducts(): Resource<RecommendedProductsResponse>
}