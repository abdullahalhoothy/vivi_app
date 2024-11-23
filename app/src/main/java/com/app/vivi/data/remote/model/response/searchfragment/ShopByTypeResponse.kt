package com.app.vivi.data.remote.model.response.searchfragment

data class ShopByTypeResponse(
    val products: List<ProductType>? = null
)

data class ProductType(
    val id: Int? = null,
    val type: String? = null,
    val imageUrl: String? = null
)
