package com.app.vivi.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class GetPatientRequest(
    @SerializedName("doctorId") val doctorId: String
)
