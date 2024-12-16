package com.app.vivi.data.remote.model.data.productfragment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDetailsData(
    val id: String?,
    val name: String?,
    val description: String?,
    val producturl: String?,
    val imageurl: String?,
    val averagerating: String?,
    val totalratings: String?,
    val discountedprice: String?,
    val discountpercentage: String?,
    val originalprice: String?,
    val city: String?,
    val country: String?,
    val countryflagurl: String?,
    val userrating: UserRating?): Parcelable{

    @Parcelize
    data class UserRating(
        val rating: String?,
        val review: String?,
        val userName: String?,
        val description: String?,
        val userProfileImageUrl: String?
    ): Parcelable
}
