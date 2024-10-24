package com.app.vivi.domain.repo

import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.response.configuration.ConfigurationResponse

interface SplashRepo {

    suspend fun getConfigurations(): Resource<ConfigurationResponse>
}