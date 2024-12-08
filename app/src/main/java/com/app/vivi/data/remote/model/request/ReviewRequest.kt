package com.app.vivi.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class ReviewRequest(
    @SerializedName("review_id") val review_id: Int)
