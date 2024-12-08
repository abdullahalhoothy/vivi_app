package com.app.vivi.domain.repo

import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.request.LoginRequest
import com.app.vivi.data.remote.model.request.ResetPasswordRequest
import com.app.vivi.data.remote.model.response.LoginResponse
import com.app.vivi.data.remote.model.response.filter.FilteredProductsResponse
import com.app.vivi.data.remote.model.response.login.ResetPasswordResponse

interface LoginRepo {

    suspend fun login(loginRequest: LoginRequest): Resource<LoginResponse>

    suspend fun getResetPasswordApi(request: ResetPasswordRequest): Resource<ResetPasswordResponse>

}