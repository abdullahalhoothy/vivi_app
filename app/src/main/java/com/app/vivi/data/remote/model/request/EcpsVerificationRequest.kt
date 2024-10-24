package com.app.vivi.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class EcpsVerificationRequest(
    @SerializedName("credential_id") val credentialId: String,
    @SerializedName("code") val code: String,
    @SerializedName("pass") val pass: String,
)