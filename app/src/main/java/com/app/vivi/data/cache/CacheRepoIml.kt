package com.app.vivi.data.cache

import com.app.vivi.data.remote.model.response.LoginResponse
import com.app.vivi.data.remote.model.response.configuration.AppConfigResponse
import com.app.vivi.domain.repo.CacheRepo
import com.app.vivi.extension.fromJson
import com.app.vivi.extension.toJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CacheRepoIml @Inject constructor(private val dataStoreHelper: DataStoreHelper) : CacheRepo {
    override suspend fun isLoggedIn(): Flow<Boolean> {
        return dataStoreHelper.getValue(PrefKeys.IS_LOGGED_IN, false)
    }

    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStoreHelper.setValue(PrefKeys.IS_LOGGED_IN, isLoggedIn)
    }

    override suspend fun saveLoginResponse(response: LoginResponse) {
        saveDoctorEmail(response.email)
        dataStoreHelper.setValue(PrefKeys.LOGIN_RESPONSE, response.toJson())
    }

    override suspend fun getLoginResponse(): Flow<LoginResponse?> {
        return dataStoreHelper.getValueOrNull(PrefKeys.LOGIN_RESPONSE)
            .map {
                it?.fromJson(LoginResponse::class.java)
            }
    }

    override suspend fun saveConfigurationData(response: AppConfigResponse?) {
        dataStoreHelper.setValue(PrefKeys.CONFIGURATION_DATA, response.toJson())
    }

    override suspend fun getConfigurationData(): Flow<AppConfigResponse?> {
        return dataStoreHelper.getValueOrNull(PrefKeys.CONFIGURATION_DATA)
            .map { it?.fromJson(AppConfigResponse::class.java) }
    }

    override suspend fun clearConfigurationData() {
        dataStoreHelper.clearData(PrefKeys.CONFIGURATION_DATA)
    }

    override suspend fun saveDoctorEmail(email: String) {
        dataStoreHelper.setValue(PrefKeys.DOCTOR_EMAIL, email)
    }

    override suspend fun getDoctorEmail(): String {
        return dataStoreHelper.getValue(PrefKeys.DOCTOR_EMAIL, "").firstOrNull().orEmpty()
    }
}