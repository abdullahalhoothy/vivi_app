package com.app.vivi.domain.repo

import com.app.vivi.data.remote.Resource
import com.app.vivi.data.remote.model.request.LoginRequest
import com.app.vivi.data.remote.model.response.LoginResponse

interface LoginRepo {

    suspend fun login(email: String, loginRequest: LoginRequest): Resource<LoginResponse>

}