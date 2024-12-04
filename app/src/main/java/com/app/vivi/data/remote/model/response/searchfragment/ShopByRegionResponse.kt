package com.app.vivi.data.remote.model.response.searchfragment

data class ShopByRegionResponse(
    val shopByRegion: List<Region>? = null
)

data class Region(
    val id: Int? = null,
    val name: String? = null,
    val imageUrl: String? = null
)
