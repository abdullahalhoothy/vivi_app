package com.app.vivi.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class DeleteMyTemplateMedicineRequest(
    @SerializedName("myTemplateMedicineId") val myTemplateMedicineId: Int?,
)