package com.app.honey.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("password") val password: String
)
