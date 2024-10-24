package com.app.vivi.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class GetSessionPrescriptionRequest(
    @SerializedName("doctorId") val doctorId: String,
    @SerializedName("patientId") val patientId: String,
    @SerializedName("type") val type: Int,
)
