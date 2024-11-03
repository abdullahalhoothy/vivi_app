package com.app.vivi.data.remote.model.response

data class FindYourNewFavoriteProductResponse(
    val products: List<NewFavoriteProduct>?
)

data class NewFavoriteProduct(
    val id: Int?,
    val name: String?,
    val searchType: String?
)
