package com.app.honey.data.remote.repo

import RecommendedProductsResponse
import com.app.honey.data.remote.ApiNames
import com.app.honey.data.remote.ApiService
import com.app.honey.data.remote.Resource
import com.app.honey.data.remote.model.request.*
import com.app.honey.data.remote.model.response.*
import com.app.honey.data.remote.model.response.configuration.ConfigurationResponse
import com.app.honey.domain.repo.CacheRepo
import com.app.honey.domain.repo.ProductRepo
import com.app.honey.domain.repo.SplashRepo
import javax.inject.Inject

class SplashRepoImpl @Inject constructor(apiService: ApiService, val cacheRepo: CacheRepo) :
    SplashRepo,
    BaseRepo(apiService) {

    override suspend fun getConfigurations(): Resource<ConfigurationResponse> {
        return safeApiCall { apiService.configuration() }
    }

}