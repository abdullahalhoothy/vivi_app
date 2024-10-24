package com.app.vivi.data.remote.repo

import com.app.vivi.data.remote.ApiNames
import com.app.vivi.data.remote.ApiService
import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.request.LoginRequest
import com.app.vivi.data.remote.model.response.LoginResponse
import com.app.vivi.domain.repo.LoginRepo
import javax.inject.Inject

class LoginRepoImpl @Inject constructor(apiService: ApiService) : LoginRepo,
    BaseRepo(apiService) {
    override suspend fun login(email: String, loginRequest: LoginRequest): Resource<LoginResponse> {
        return safeApiCall(email, loginRequest, ApiNames.LOGIN)

        /*val jsonRequest = JsonParser.parseString(loginRequest.toJson()).asJsonObject

        val call = apiService.postRequest(ApiNames.LOGIN, email, jsonRequest)

        try {
            if (call.isSuccessful && call.body() != null) {
                Log.e("LoginRepoImpl", call.body().toString())
            } else {

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return Resource.Error("Just testing")*/
        /*return safeApiCall(
            call = { apiService.login(email, loginRequest) }
        )*/
    }

}