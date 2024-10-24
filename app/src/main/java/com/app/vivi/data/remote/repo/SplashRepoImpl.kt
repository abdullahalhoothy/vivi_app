package com.app.vivi.data.remote.repo

import com.app.vivi.data.remote.ApiService
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.response.configuration.ConfigurationResponse
import com.app.vivi.domain.repo.CacheRepo
import com.app.vivi.domain.repo.SplashRepo
import javax.inject.Inject

class SplashRepoImpl @Inject constructor(apiService: ApiService, val cacheRepo: CacheRepo) :
    SplashRepo,
    BaseRepo(apiService) {

    override suspend fun getConfigurations(): Resource<ConfigurationResponse> {
        return safeApiCall { apiService.configuration() }
    }

}