package com.app.honey.domain.repo

import com.app.honey.data.remote.Resource
import com.app.honey.data.remote.model.response.configuration.ConfigurationResponse

interface SplashRepo {

    suspend fun getConfigurations(): Resource<ConfigurationResponse>
}