package com.app.vivi.data.remote.model.response

data class UserReviewsResponse(
    val reviews: List<Review>?
)

data class Review(
    val review_id: Int?,
    val name: String?,
    val userProfileImageUrl: String?,
    val description: String?,
    val averageRating: String?,
    val totalRatings: Int?,
    val time: String?,
    val totalLikes: String?,
    val totalComments: String?
)
