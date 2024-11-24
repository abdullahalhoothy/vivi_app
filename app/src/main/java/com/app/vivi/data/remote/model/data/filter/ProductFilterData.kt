package com.app.vivi.data.remote.model.data.filter

data class ProductFilterData(
    val id: Int,
    val name: String,
    val description: String,
    val address: String,
    val discountPrice: String,
    val originalPrice: String,
    val rating: Float,
    val ratingsCount: String,
    val backgroundImage: Int, // Resource ID for background image
    val bottleImage: Int,     // Resource ID for bottle image
    val userImage: Int,       // Resource ID for user image
    val recommendation: String,
    val ratingUser: String
)