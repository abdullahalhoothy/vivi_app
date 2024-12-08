package com.app.vivi.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class ReviewsRequest(
    @SerializedName("type") val type: String)
