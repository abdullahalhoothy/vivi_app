package com.app.vivi.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)
