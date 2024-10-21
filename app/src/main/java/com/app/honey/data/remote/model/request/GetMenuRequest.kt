package com.app.honey.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class GetMenuRequest(
    @SerializedName("patientId") val patientId: Int
)
