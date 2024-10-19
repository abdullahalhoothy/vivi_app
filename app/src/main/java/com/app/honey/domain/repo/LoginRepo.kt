package com.app.honey.domain.repo

import com.app.honey.data.remote.Resource
import com.app.honey.data.remote.model.request.LoginRequest
import com.app.honey.data.remote.model.response.LoginResponse

interface LoginRepo {

    suspend fun login(email: String, loginRequest: LoginRequest): Resource<LoginResponse>

}