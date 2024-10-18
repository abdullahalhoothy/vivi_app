package com.app.honey.domain.repo

import com.app.honey.data.remote.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface CacheRepo {

    suspend fun isLoggedIn(): Flow<Boolean>

    suspend fun setLoggedIn(isLoggedIn: Boolean)

    suspend fun saveDoctorEmail(email: String)

    suspend fun getDoctorEmail(): String

    suspend fun saveLoginResponse(response: LoginResponse)

    suspend fun getLoginResponse(): Flow<LoginResponse?>
}