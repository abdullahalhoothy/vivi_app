package com.app.vivi.data.remote.model.response.searchfragment

data class CountriesResponse(
    val countries: List<Country>? = null
)

data class Country(
    val id: Int? = null,
    val name: String? = null,
    val imageUrl: String? = null
)
