package com.app.honey.data.remote.model.data.productfragment

data class HoneyData(
    val name: String,
    val address: String,
    val price: String,
    val rating: Double,
    val ratingValue: Double,
    val totalRatings: String,
    val ratingDescription: String,
    val discount: Int
)
