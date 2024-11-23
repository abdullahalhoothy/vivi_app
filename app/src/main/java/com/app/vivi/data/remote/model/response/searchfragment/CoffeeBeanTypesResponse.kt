package com.app.vivi.data.remote.model.response.searchfragment

data class CoffeeBeanTypesResponse(
    val coffeeBeanTypes: List<CoffeeBeanType>? = null
)

data class CoffeeBeanType(
    val id: Int? = null,
    val name: String? = null,
    val colorCode: String? = null
)
