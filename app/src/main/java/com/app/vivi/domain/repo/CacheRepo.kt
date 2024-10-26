package com.app.vivi.domain.repo

import com.app.vivi.data.remote.model.response.LoginResponse
import com.app.vivi.data.remote.model.response.configuration.AppConfigResponse
import kotlinx.coroutines.flow.Flow

interface CacheRepo {

    suspend fun isLoggedIn(): Flow<Boolean>

    suspend fun setLoggedIn(isLoggedIn: Boolean)

    suspend fun saveDoctorEmail(email: String)

    suspend fun getDoctorEmail(): String

    suspend fun saveLoginResponse(response: LoginResponse)

    suspend fun getLoginResponse(): Flow<LoginResponse?>

    suspend fun saveConfigurationData(response: AppConfigResponse?)

    suspend fun getConfigurationData(): Flow<AppConfigResponse?>
}