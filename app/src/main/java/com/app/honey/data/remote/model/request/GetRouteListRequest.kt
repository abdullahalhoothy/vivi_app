package com.app.honey.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class GetRouteListRequest(
    @SerializedName("patientId") val patientId: String,
    @SerializedName("doctorId") val doctorId: String,

)
