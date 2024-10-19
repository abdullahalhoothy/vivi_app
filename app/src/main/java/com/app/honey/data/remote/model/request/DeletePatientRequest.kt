package com.app.honey.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class DeletePatientRequest(
    @SerializedName("doctorId") val doctorId: Int,
    @SerializedName("patientId") val patientId: Int,
) {
}